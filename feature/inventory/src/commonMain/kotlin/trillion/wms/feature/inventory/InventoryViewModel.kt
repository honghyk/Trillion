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
import trillion.wms.core.domain.DeleteFabricRollUseCase
import trillion.wms.core.domain.GetZonesStreamUseCase
import trillion.wms.core.domain.SearchFabricRollsStreamUseCase
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.RefreshableUiResultFlow
import trillion.wms.core.ui.utils.UiMessageManager
import trillion.wms.core.ui.utils.asUiResult
import trillion.wms.feature.inventory.SearchUiState.Filters
import trillion.wms.feature.inventory.SearchUiState.ZoneFilter

class InventoryViewModel(
    getZonesStream: GetZonesStreamUseCase,
    private val searchFabricRollsStream: SearchFabricRollsStreamUseCase,
    private val deleteFabricRoll: DeleteFabricRollUseCase,
) : ViewModel() {

    private val uiMessageManager = UiMessageManager()

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

    val searchResults = combine(
        searchQuery.debounce { 300L },
        selectedZoneFilter,
        ::Pair
    ).flatMapLatest { (query, zoneFilter) ->
        searchFabricRollsStream(
            query = query,
            zoneId = zoneFilter.zoneId,
        )
    }.asUiResult()

    private val searchUiState = combine(
        searchQuery,
        filters,
        lengthUnit,
        searchResults,
        ::SearchUiState
    )

    val uiState = combine(
        searchUiState,
        uiMessageManager.message,
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
            deleteFabricRoll(fabricRoll.id)
                .fold(
                    onSuccess = { uiMessageManager.emitMessage("롤이 삭제되었습니다") },
                    onFailure = { uiMessageManager.emitMessage("롤을 삭제하지 못했습니다") }
                )
        }
    }

    fun clearMessage(id: Long) {
        uiMessageManager.clearMessage(id)
    }
}
