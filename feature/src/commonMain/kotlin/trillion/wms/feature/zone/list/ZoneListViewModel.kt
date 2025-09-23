package trillion.wms.feature.zone.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import trillion.wms.core.ui.utils.RefreshableUiResultFlow
import trillion.wms.core.ui.utils.cancellableRunCatching
import trillion.wms.feature.zone.list.ZoneListUiState.SideEffect
import trillion.wms.core.domain.DeleteZoneUseCase
import trillion.wms.core.domain.GetZonesStreamUseCase
import trillion.wms.core.model.Zone

class ZoneListViewModel(
    getZonesStream: GetZonesStreamUseCase,
    private val deleteZone: DeleteZoneUseCase,
) : ViewModel() {

    val zones = RefreshableUiResultFlow(
        produce = { getZonesStream(forceRefresh = true) }
    )
    val sideEffect = MutableStateFlow<SideEffect?>(null)

    val uiState: StateFlow<ZoneListUiState> = combine(
        zones.flow,
        zones.isRefreshing,
        sideEffect,
        ::ZoneListUiState
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ZoneListUiState()
    )

    fun deleteZone(zone: Zone) {
        viewModelScope.launch {
            cancellableRunCatching { deleteZone(zone.id) }
                .onFailure { sideEffect.emit(SideEffect.ShowSnackbar("구역을 삭제하지 못했습니다.")) }
        }
    }

    fun refresh() {
        zones.refresh()
    }

    fun onSideEffectConsumed() {
        sideEffect.value = null
    }
}
