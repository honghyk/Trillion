package trillion.wms.feature.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.RefreshableUiResultFlow
import trillion.wms.core.ui.utils.cancellableRunCatching
import trillion.wms.feature.inventory.InventoryUiState.SideEffect
import trillion.wms.feature.inventory.SearchUiState.*
import trillion.wms.core.domain.DeleteFabricRollUseCase
import trillion.wms.core.domain.GetInventoryOverViewStreamUseCase
import trillion.wms.core.domain.GetZonesStreamUseCase
import trillion.wms.core.domain.SearchFabricRollsStreamUseCase
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.InventorySummary

class InventoryViewModel(
    getZonesStream: GetZonesStreamUseCase,
    getInventoryOverViewStream: GetInventoryOverViewStreamUseCase,
    private val searchFabricRollsStream: SearchFabricRollsStreamUseCase,
    private val deleteFabricRoll: DeleteFabricRollUseCase,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val lengthUnit = MutableStateFlow(LengthUnit.METER)

    private val selectedZoneFilter = MutableStateFlow<ZoneFilter>(ZoneFilter.All)
    private val availableZoneFilters: Flow<List<ZoneFilter>> = getZonesStream()
        .catch { emit(emptyList()) }
        .map {
            listOf(ZoneFilter.All) + it.map { zone -> ZoneFilter.Selected(zone) }
        }
    private val filters: Flow<Filters> = combine(
        selectedZoneFilter,
        availableZoneFilters,
        ::Filters
    )

    private val inventorySummary = RefreshableUiResultFlow(
        produce = {
            getInventoryOverViewStream(forceRefresh = true)
                .map { it ?: InventorySummary.EMPTY }
        }
    )
    val searchResults = RefreshableUiResultFlow(
        produce = {
            combine(
                searchQuery.debounce { 300L },
                selectedZoneFilter,
                ::Pair
            ).flatMapLatest { (query, zoneFilter) ->
                searchFabricRollsStream(
                    query = query,
                    zoneId = if (zoneFilter is ZoneFilter.Selected) zoneFilter.zone.id else null
                )
            }
        }
    )

    private val inventorySummaryUiState = combine(
        lengthUnit,
        inventorySummary.flow,
        ::InventorySummaryUiState
    )

    private val searchUiState = combine(
        searchQuery,
        filters,
        lengthUnit,
        searchResults.flow,
        ::SearchUiState
    )

    private val isRefreshing = combine(
        inventorySummary.isRefreshing,
        searchResults.isRefreshing
    ) { refreshingStates -> refreshingStates.any { it } }

    private val sideEffect = MutableStateFlow<SideEffect?>(null)

    val uiState = combine(
        inventorySummaryUiState,
        searchUiState,
        isRefreshing,
        sideEffect,
        ::InventoryUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InventoryUiState()
    )

    fun updateZoneFilter(zoneFilter: ZoneFilter) {
        this.selectedZoneFilter.value = zoneFilter
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun updateDisplayLengthUnit(lengthUnit: LengthUnit) {
        this.lengthUnit.value = lengthUnit
    }

    fun deleteFabricRoll(fabricRoll: FabricRoll) {
        viewModelScope.launch {
            cancellableRunCatching { deleteFabricRoll(fabricRoll.id) }
                .onSuccess { sideEffect.emit(SideEffect.ShowSnackbar("롤이 삭제되었습니다.")) }
                .onFailure { sideEffect.emit(SideEffect.ShowSnackbar("롤을 삭제하지 못했습니다.")) }
        }
    }

    fun refresh() {
        searchResults.refresh()
        inventorySummary.refresh()
    }

    fun onSideEffectConsumed() {
        sideEffect.value = null
    }
}
