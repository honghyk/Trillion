package trillion.wms.feature.zone.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import trillion.wms.core.ui.utils.RefreshableUiResultFlow
import trillion.wms.core.ui.utils.cancellableRunCatching
import trillion.wms.core.domain.DeleteZoneUseCase
import trillion.wms.core.domain.GetZonesStreamUseCase
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.utils.UiMessageManager

class ZoneListViewModel(
    getZonesStream: GetZonesStreamUseCase,
    private val deleteZone: DeleteZoneUseCase,
) : ViewModel() {

    private val uiMessageManager = UiMessageManager()

    private val zones = RefreshableUiResultFlow(
        produce = { getZonesStream(forceRefresh = true) }
    )

    val uiState: StateFlow<ZoneListUiState> = combine(
        zones.flow,
        zones.isRefreshing,
        uiMessageManager.message,
        ::ZoneListUiState
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ZoneListUiState()
    )

    fun deleteZone(zone: Zone) {
        viewModelScope.launch {
            deleteZone(zone.id)
                .fold(
                    onSuccess = { uiMessageManager.emitMessage("${zone.name}} 구역을 삭제 했습니다") },
                    onFailure = { uiMessageManager.emitMessage("구역을 삭제하지 못했습니다.") }
                )
        }
    }

    fun refresh() {
        zones.refresh()
    }

    fun clearMessage(id: Long) {
        uiMessageManager.clearMessage(id)
    }
}
