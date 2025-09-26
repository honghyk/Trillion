package trillion.wms.feature.fabricroll.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
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
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.cancellableRunCatching
import trillion.wms.core.ui.utils.formatDecimal

class FabricRollFormViewModel(
    zoneId: Long?,
    rollId: Long?,
    getFabricRollStream: GetFabricRollStreamUseCase,
    private val getZonesStream: GetZonesStreamUseCase,
    private val addFabricRoll: AddFabricRollUseCase,
    private val updateFabricRoll: UpdateFabricRollUseCase,
) : ViewModel() {

    private val isInEditMode: Boolean = rollId != null

    private val _uiState = MutableStateFlow(FabricRollFormUiState(isInEditMode = isInEditMode))
    val uiState: StateFlow<FabricRollFormUiState> = _uiState.asStateFlow()

    private val fabricRoll: Flow<FabricRoll?> = if (rollId == null) {
        flowOf(null)
    } else {
        getFabricRollStream(rollId)
    }

    init {
        viewModelScope.launch {
            combine(
                getZonesStream(),
                fabricRoll,
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
                rollIdField = it.rollIdField.copy(
                    value = value,
                    serverErrorMessage = null
                )
            )
        }
    }

    fun updateItemNo(value: String) {
        _uiState.update {
            it.copy(itemNoField = it.itemNoField.copy(value = value))
        }
    }

    fun updateOrderNo(value: String) {
        _uiState.update {
            it.copy(orderNoField = it.orderNoField.copy(value = value))
        }
    }

    fun updateColor(value: String) {
        _uiState.update {
            it.copy(colorField = it.colorField.copy(value = value))
        }
    }

    fun updateFactory(value: String) {
        _uiState.update {
            it.copy(factoryField = it.factoryField.copy(value = value))
        }
    }

    fun updateFinish(value: String) {
        _uiState.update {
            it.copy(finishField = it.finishField.copy(value = value))
        }
    }

    fun updateRemark(value: String) {
        _uiState.update {
            it.copy(remarkField = it.remarkField.copy(value = value))
        }
    }

    fun updateQuantity(value: String) {
        _uiState.update {
            it.copy(quantityField = it.quantityField.copy(value = value))
        }
    }

    fun updateLengthUnit(value: LengthUnit) {
        _uiState.update {
            it.copy(lengthUnit = value)
        }
    }

    fun submit() {
        val currentState = uiState.value
        if (currentState.formSubmitState == FormSubmitState.IN_PROGRESS) return

        viewModelScope.launch {
            _uiState.update { it.copy(formSubmitState = FormSubmitState.IN_PROGRESS) }

            val request = buildRequest()
            val submitResult = when (request) {
                is AddFabricRollRequest -> addFabricRoll(request)
                is UpdateFabricRollRequest -> updateFabricRoll(request)
            }
            submitResult
                .fold(
                    onSuccess = {
                        _uiState.update { it.copy(formSubmitState = FormSubmitState.SUBMITTED) }
                    },
                    onFailure = { e ->
                        _uiState.update {
                            it.copy(
                                rollIdField = it.rollIdField.copy(serverErrorMessage = e.getErrorMessage()),
                                formSubmitState = FormSubmitState.IDLE,
                            )
                        }
                    }
                )
        }
    }

    private fun buildRequest(): FabricRollMutationRequest {
        val currentState = uiState.value
        val id = currentState.rollIdField.value.toLong()
        val zoneId = currentState.selectedZone?.id
        val itemNo = currentState.itemNoField.value
        val orderNo = currentState.orderNoField.value
        val color = currentState.colorField.value
        val factory = currentState.factoryField.value
        val finish = currentState.finishField.value
        val remark = currentState.remarkField.value
        val quantity =
            currentState.quantityField.value.toDouble() / currentState.lengthUnit.multiplier

        return if (isInEditMode) {
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
        } else {
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
        }
    }

    private fun Throwable.getErrorMessage(): String? {
        return when (this) {
            is AlreadyExistsException -> "같은 번호 롤이 이미 존재합니다"
            else -> null
        }
    }

    private fun FabricRollFormUiState.toLoadedState(
        zones: List<Zone>,
        defaultZoneId: Long?,
        fabricRoll: FabricRoll?,
    ): FabricRollFormUiState = copy(
        zones = zones,
        selectedZone = zones.firstOrNull { it.id == (fabricRoll?.zoneId ?: defaultZoneId) },
        rollIdField = rollIdField.copy(value = fabricRoll?.id?.toString() ?: ""),
        itemNoField = itemNoField.copy(value = fabricRoll?.itemNo ?: ""),
        orderNoField = orderNoField.copy(value = fabricRoll?.orderNo ?: ""),
        colorField = colorField.copy(value = fabricRoll?.color ?: ""),
        factoryField = factoryField.copy(value = fabricRoll?.factory ?: ""),
        finishField = finishField.copy(value = fabricRoll?.finish ?: ""),
        quantityField = quantityField.copy(
            value = if (fabricRoll?.quantity != null) {
                (fabricRoll.quantity * lengthUnit.multiplier).formatDecimal(1)
            } else {
                ""
            }
        ),
        remarkField = remarkField.copy(value = fabricRoll?.remark ?: ""),
    )
}
