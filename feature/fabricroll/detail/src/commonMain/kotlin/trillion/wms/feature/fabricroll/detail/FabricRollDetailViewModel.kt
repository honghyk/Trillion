package trillion.wms.feature.fabricroll.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import trillion.wms.core.domain.DeleteOutboundHistoryUseCase
import trillion.wms.core.domain.GetFabricRollStreamUseCase
import trillion.wms.core.domain.GetOutboundHistoriesUseCase
import trillion.wms.core.domain.GetZoneStreamUseCase
import trillion.wms.core.domain.GetZoneStreamUseCase.Params
import trillion.wms.core.domain.RefreshFabricRollUseCase
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.RefreshableUiResultFlow
import trillion.wms.core.ui.utils.UiMessageManager
import trillion.wms.core.ui.utils.asUiResult
import trillion.wms.core.ui.utils.combine

class FabricRollDetailViewModel(
    getZoneStream: GetZoneStreamUseCase,
    getFabricRollStream: GetFabricRollStreamUseCase,
    getOutboundHistoriesStream: GetOutboundHistoriesUseCase,
    private val rollId: Long,
    private val deleteOutboundHistory: DeleteOutboundHistoryUseCase,
    private val refreshFabricRoll: RefreshFabricRollUseCase,
) : ViewModel() {

    private val uiMessageManager = UiMessageManager()

    private val lengthUnit: MutableStateFlow<LengthUnit> = MutableStateFlow(LengthUnit.METER)

    private val zone = getZoneStream(Params.RollId(rollId))
        .map { it ?: Zone.EMPTY }

    private val fabricRoll = getFabricRollStream(rollId)
        .map { requireNotNull(it) }
        .asUiResult()

    private val outboundHistories = RefreshableUiResultFlow(
        produce = { getOutboundHistoriesStream(rollId, forceFresh = true) }
    )
    private val isRefreshing = combine(
        refreshFabricRoll.inProgress,
        outboundHistories.isRefreshing,
    ) { refreshingStates -> refreshingStates.any { it } }

    val uiState: StateFlow<FabricRollDetailUiState> = combine(
        zone,
        fabricRoll,
        outboundHistories.flow,
        lengthUnit,
        isRefreshing,
        uiMessageManager.message,
        ::FabricRollDetailUiState
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FabricRollDetailUiState()
    )

    init {
        refresh(false)
    }

    fun refresh(fromUser: Boolean) {
        viewModelScope.launch {
            refreshFabricRoll(RefreshFabricRollUseCase.Params(rollId, fromUser))
        }
        outboundHistories.refresh()
    }

    fun updateLengthUnit(lengthUnit: LengthUnit) {
        this.lengthUnit.value = lengthUnit
    }

    fun deleteOutboundHistory(outboundHistory: OutboundHistory) {
        viewModelScope.launch {
            deleteOutboundHistory(outboundHistory.id)
                .fold(
                    onSuccess = { uiMessageManager.emitMessage("출고 내역을 삭제 했습니다") },
                    onFailure = { uiMessageManager.emitMessage("출고 내역을 삭제하지 못했습니다") }
                )
        }
    }

    fun clearMessage(id: Long) {
        uiMessageManager.clearMessage(id)
    }
}
