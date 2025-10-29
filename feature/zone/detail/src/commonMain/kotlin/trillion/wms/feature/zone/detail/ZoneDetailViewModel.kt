package trillion.wms.feature.zone.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import trillion.wms.core.domain.DeleteFabricRollUseCase
import trillion.wms.core.domain.GetZoneStreamUseCase
import trillion.wms.core.domain.GetZoneStreamUseCase.Params
import trillion.wms.core.domain.SearchFabricRollsStreamUseCase
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.RefreshableUiResultFlow
import trillion.wms.core.ui.utils.UiMessageManager
import trillion.wms.core.ui.utils.cancellableRunCatching
import trillion.wms.core.ui.utils.combine

class ZoneDetailViewModel(
    zoneId: Long,
    getZoneStream: GetZoneStreamUseCase,
    searchFabricRollsStream: SearchFabricRollsStreamUseCase,
    private val deleteFabricRoll: DeleteFabricRollUseCase,
) : ViewModel() {

    private val uiMessageManager = UiMessageManager()

    private val searchQuery = MutableStateFlow("")
    private val lengthUnit = MutableStateFlow(LengthUnit.METER)
    private val zone = RefreshableUiResultFlow(
        produce = {
            getZoneStream(Params.ZoneId(zoneId), forceRefresh = true)
                .map { requireNotNull(it) }
        }
    )
    private val fabricRolls = RefreshableUiResultFlow(
        produce = {
            searchQuery
                .debounce { 300L }
                .flatMapLatest { searchFabricRollsStream(it, zoneId) }
        }
    )
    private val isRefreshing = combine(
        zone.isRefreshing,
        fabricRolls.isRefreshing,
    ) { refreshingStates -> refreshingStates.any { it } }

    val uiState: StateFlow<ZoneDetailUiState> = combine(
        zone.flow,
        fabricRolls.flow,
        searchQuery,
        lengthUnit,
        isRefreshing,
        uiMessageManager.message,
        ::ZoneDetailUiState
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ZoneDetailUiState()
    )

    fun refresh() {
        zone.refresh()
        fabricRolls.refresh()
    }

    fun updateLengthUnit(lengthUnit: LengthUnit) {
        this.lengthUnit.value = lengthUnit
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun deleteFabricRoll(fabricRoll: FabricRoll) {
        viewModelScope.launch {
            cancellableRunCatching { deleteFabricRoll(fabricRoll.id) }
                .fold(
                    onSuccess = {
                        uiMessageManager.emitMessage("롤을 삭제 했습니다.")
                    },
                    onFailure = {
                        uiMessageManager.emitMessage("롤을 삭제하지 못했습니다.")
                    }
                )
        }
    }

    fun clearMessage(id: Long) {
        uiMessageManager.clearMessage(id)
    }
}
