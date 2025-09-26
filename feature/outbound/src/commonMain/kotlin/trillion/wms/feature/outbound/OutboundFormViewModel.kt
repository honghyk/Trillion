package trillion.wms.feature.outbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import trillion.wms.core.domain.GetFabricRollStreamUseCase
import trillion.wms.core.domain.OutboundFabricRollUseCase
import trillion.wms.core.model.OutboundRequest
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.LengthUnit

class OutboundFormViewModel(
    private val rollId: Long,
    private val getFabricRollStreamUseCase: GetFabricRollStreamUseCase,
    private val outboundFabricRoll: OutboundFabricRollUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OutboundFormUiState())
    val uiState: MutableStateFlow<OutboundFormUiState> = _uiState

    init {
        viewModelScope.launch {
            getFabricRollStreamUseCase(rollId).first()?.let { fabricRoll ->
                _uiState.update {
                    it.copy(
                        itemNo = fabricRoll.itemNo,
                        orderNo = fabricRoll.orderNo.ifEmpty { "-" },
                        availableQtyInMeters = fabricRoll.remainingQuantity,
                    )
                }
            }
        }
    }

    fun onQtyToProcessChange(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                quantityToProcess = value
            )
        }
    }

    fun onLengthUnitChange(lengthUnit: LengthUnit) {
        _uiState.update { uiState ->
            uiState.copy(
                lengthUnit = lengthUnit,
            )
        }
    }

    fun onBuyerChange(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                buyerField = uiState.buyerField.copy(value = value)
            )
        }
    }

    fun onDateChange(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                dateField = uiState.dateField.copy(value = value)
            )
        }
    }

    fun onRemarkChange(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                remarkField = uiState.remarkField.copy(value = value)
            )
        }
    }

    fun submit() {
        if (uiState.value.formSubmitState == FormSubmitState.IN_PROGRESS) return

        viewModelScope.launch {
            _uiState.update { it.copy(formSubmitState = FormSubmitState.IN_PROGRESS) }

            val request = buildOutboundRequest()
            outboundFabricRoll(request)
                .fold(
                    onSuccess = { _uiState.update { it.copy(formSubmitState = FormSubmitState.SUBMITTED) } },
                    onFailure = { _uiState.update { it.copy(formSubmitState = FormSubmitState.IDLE) } }
                )
        }
    }

    private fun buildOutboundRequest(): OutboundRequest {
        val currentState = uiState.value

        val dateFormat = currentState.dateField.format
        val dateInput = currentState.dateField.value
        val date = dateFormat.parse(dateInput).atStartOfDayIn(TimeZone.currentSystemDefault())

        val lengthUnit = currentState.lengthUnit
        val quantity = (currentState.qtyToProcessField.value.toDouble()) / lengthUnit.multiplier

        return OutboundRequest(
            rollId = rollId,
            qtyToProcess = quantity,
            date = date,
            buyer = currentState.buyerField.value,
            remark = currentState.remarkField.value,
        )
    }
}
