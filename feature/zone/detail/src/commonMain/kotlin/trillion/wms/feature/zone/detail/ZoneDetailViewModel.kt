package trillion.wms.feature.zone.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import trillion.wms.core.domain.DeleteFabricRollUseCase
import trillion.wms.core.domain.GetFabricRollsStreamUseCase
import trillion.wms.core.domain.GetZoneStreamUseCase
import trillion.wms.core.domain.GetZoneStreamUseCase.Params
import trillion.wms.core.domain.RefreshFabricRollsUseCase
import trillion.wms.core.domain.RefreshZoneUseCase
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.UiMessageManager
import trillion.wms.core.ui.utils.asUiResult
import trillion.wms.core.ui.utils.cancellableRunCatching
import trillion.wms.core.ui.utils.combine

class ZoneDetailViewModel(
    getZoneStream: GetZoneStreamUseCase,
    getFabricRollsStream: GetFabricRollsStreamUseCase,
    private val zoneId: Long,
    private val refreshZone: RefreshZoneUseCase,
    private val refreshFabricRolls: RefreshFabricRollsUseCase,
    private val deleteFabricRoll: DeleteFabricRollUseCase,
) : ViewModel() {

    private val uiMessageManager = UiMessageManager()

    private val searchQuery = MutableStateFlow("")
    private val lengthUnit = MutableStateFlow(LengthUnit.METER)
    private val zone = getZoneStream(Params.ZoneId(zoneId))
        .map { requireNotNull(it) }
        .asUiResult()

    private val fabricRolls = searchQuery
        .debounce { 300L }
        .flatMapLatest { query ->
            getFabricRollsStream(zoneId).map { fabricRolls ->
                if (query.isEmpty()) {
                    fabricRolls
                } else {
                    fabricRolls.filterByQuery(query)
                }
            }
        }
        .flowOn(Dispatchers.Default)
        .asUiResult()

    private val isRefreshing = combine(
        refreshZone.inProgress,
        refreshFabricRolls.inProgress,
    ) { refreshingStates -> refreshingStates.any { it } }

    val uiState: StateFlow<ZoneDetailUiState> = combine(
        zone,
        fabricRolls,
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

    init {
        refresh(false)
    }

    fun refresh(fromUser: Boolean) {
        viewModelScope.launch {
            refreshZone(RefreshZoneUseCase.Params(zoneId, fromUser))
        }
        viewModelScope.launch {
            refreshFabricRolls(RefreshFabricRollsUseCase.Params(zoneId, fromUser))
        }
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
                    onSuccess = { uiMessageManager.emitMessage("롤을 삭제 했습니다.") },
                    onFailure = { uiMessageManager.emitMessage("롤을 삭제하지 못했습니다.") }
                )
        }
    }

    fun clearMessage(id: Long) {
        uiMessageManager.clearMessage(id)
    }

    private fun List<FabricRoll>.filterByQuery(query: String): List<FabricRoll> {
        return filter { fabricRoll ->
            fabricRoll.id.toString().contains(query) ||
                    fabricRoll.itemNo.contains(query) ||
                    fabricRoll.orderNo.contains(query) ||
                    fabricRoll.color.contains(query) ||
                    fabricRoll.factory.contains(query) ||
                    fabricRoll.finish.contains(query)
        }
    }
}
