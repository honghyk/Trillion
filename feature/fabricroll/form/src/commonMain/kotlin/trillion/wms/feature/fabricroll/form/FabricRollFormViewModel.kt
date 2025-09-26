package trillion.wms.feature.fabricroll.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import trillion.wms.core.domain.AddFabricRollUseCase
import trillion.wms.core.domain.GetFabricRollStreamUseCase
import trillion.wms.core.domain.GetZonesStreamUseCase
import trillion.wms.core.domain.UpdateFabricRollUseCase
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.FabricRollMutationRequest
import trillion.wms.core.model.UpdateFabricRollRequest
import trillion.wms.core.model.Zone
import trillion.wms.core.model.exception.AlreadyExistsException
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.cancellableRunCatching
import trillion.wms.core.ui.utils.formatDecimal

class FabricRollFormViewModel(
    private val zoneId: Long,
    private val rollId: Long?,
    private val getZonesStream: GetZonesStreamUseCase,
    private val getFabricRollStream: GetFabricRollStreamUseCase,
    private val addFabricRoll: AddFabricRollUseCase,
    private val updateFabricRoll: UpdateFabricRollUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FabricRollFormUiState(isEdit = rollId != null))
    val uiState: StateFlow<FabricRollFormUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getZonesStream(),
                if (rollId != null) getFabricRollStream(rollId) else flowOf(null),
                ::Pair
            ).collectLatest { (zones, fabricRoll) ->
                _uiState.update { it.toLoadedState(zones, zoneId, fabricRoll) }
            }
        }
    }

    fun updateSelectedZone(zone: Zone) {
        _uiState.update {
            it.copy(selectedZone = zone)
        }
    }

    fun updateRollId(value: String) {
        _uiState.update {
            it.copy(
                rollIdFieldState = it.rollIdFieldState.copy(
                    value = value,
                    serverErrorMessage = null
                )
            )
        }
    }

    fun updateItemNo(value: String) {
        _uiState.update {
            it.copy(itemNoFieldState = it.itemNoFieldState.copy(value = value))
        }
    }

    fun updateOrderNo(value: String) {
        _uiState.update {
            it.copy(orderNoFieldState = it.orderNoFieldState.copy(value = value))
        }
    }

    fun updateColor(value: String) {
        _uiState.update {
            it.copy(colorFieldState = it.colorFieldState.copy(value = value))
        }
    }

    fun updateFactory(value: String) {
        _uiState.update {
            it.copy(factoryFieldState = it.factoryFieldState.copy(value = value))
        }
    }

    fun updateFinish(value: String) {
        _uiState.update {
            it.copy(finishFieldState = it.finishFieldState.copy(value = value))
        }
    }

    fun updateRemark(value: String) {
        _uiState.update {
            it.copy(remarkFieldState = it.remarkFieldState.copy(value = value))
        }
    }

    fun updateQuantity(value: String) {
        _uiState.update {
            it.copy(quantityFieldState = it.quantityFieldState.copy(value = value))
        }
    }

    fun updateLengthUnit(value: LengthUnit) {
        _uiState.update {
            it.copy(lengthUnit = value)
        }
    }

    fun onSideEffectConsumed() {
        _uiState.update {
            it.copy(sideEffect = null)
        }
    }

    fun submit() {
        viewModelScope.launch {
            _uiState.update { it.copy(submitInProgress = true) }
            cancellableRunCatching {
                when (val request = buildRequest()) {
                    is AddFabricRollRequest -> addFabricRoll(request)
                    is UpdateFabricRollRequest -> updateFabricRoll(request)
                }
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        submitInProgress = false,
                        sideEffect = FabricRollFormUiState.SideEffect.Dismiss
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        rollIdFieldState = it.rollIdFieldState.copy(
                            serverErrorMessage = when (e) {
                                is AlreadyExistsException -> "같은 번호 롤이 이미 존재합니다"
                                else -> null
                            }
                        ),
                        submitInProgress = false,
                        sideEffect = FabricRollFormUiState.SideEffect.ShowSnackbar(
                            message = when (e) {
                                is AlreadyExistsException -> "같은 번호 롤이 이미 존재합니다"
                                else -> "에러가 발생 했습니다"
                            }
                        )
                    )
                }
            }
        }
    }

    private fun buildRequest(): FabricRollMutationRequest {
        val id = uiState.value.rollIdFieldState.value.toLong()
        val zoneId = uiState.value.selectedZone?.id
        val itemNo = uiState.value.itemNoFieldState.value
        val orderNo = uiState.value.orderNoFieldState.value
        val color = uiState.value.colorFieldState.value
        val factory = uiState.value.factoryFieldState.value
        val finish = uiState.value.finishFieldState.value
        val remark = uiState.value.remarkFieldState.value
        val quantity =
            uiState.value.quantityFieldState.value.toDouble() / uiState.value.lengthUnit.multiplier

        return if (rollId == null) {
            AddFabricRollRequest(
                id = id,
                zoneId = zoneId!!,
                itemNo = itemNo,
                orderNo = orderNo,
                color = color,
                factory = factory,
                finish = finish,
                remark = remark,
                quantity = quantity,
            )
        } else {
            UpdateFabricRollRequest(
                id = id,
                zoneId = zoneId,
                itemNo = itemNo,
                orderNo = orderNo,
                color = color,
                factory = factory,
                finish = finish,
                remark = remark,
                quantity = quantity,
            )
        }
    }

    private fun FabricRollFormUiState.toLoadedState(
        zones: List<Zone>,
        defaultZoneId: Long,
        fabricRoll: FabricRoll?,
    ): FabricRollFormUiState = copy(
        zones = zones,
        selectedZone = zones.firstOrNull { it.id == (fabricRoll?.zoneId ?: defaultZoneId) },
        rollIdFieldState = rollIdFieldState.copy(value = fabricRoll?.id?.toString() ?: ""),
        itemNoFieldState = itemNoFieldState.copy(value = fabricRoll?.itemNo ?: ""),
        orderNoFieldState = orderNoFieldState.copy(value = fabricRoll?.orderNo ?: ""),
        colorFieldState = colorFieldState.copy(value = fabricRoll?.color ?: ""),
        factoryFieldState = factoryFieldState.copy(value = fabricRoll?.factory ?: ""),
        finishFieldState = finishFieldState.copy(value = fabricRoll?.finish ?: ""),
        quantityFieldState = quantityFieldState.copy(
            value = if (fabricRoll?.quantity != null) {
                (fabricRoll.quantity * lengthUnit.multiplier).formatDecimal(1)
            } else {
                ""
            }
        ),
        remarkFieldState = remarkFieldState.copy(value = fabricRoll?.remark ?: ""),
    )
}
