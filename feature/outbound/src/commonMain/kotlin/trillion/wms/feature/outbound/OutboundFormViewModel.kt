package trillion.wms.feature.outbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import trillion.wms.core.ui.utils.cancellableRunCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.model.Validators
import trillion.wms.core.ui.utils.formatDecimal
import trillion.wms.core.domain.GetFabricRollStreamUseCase
import trillion.wms.core.domain.OutboundFabricRollUseCase
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundRequest

class OutboundFormViewModel(
    private val rollId: Long,
    private val getFabricRollStreamUseCase: GetFabricRollStreamUseCase,
    private val outboundFabricRollUseCase: OutboundFabricRollUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OutboundFormUiState())
    val uiState: MutableStateFlow<OutboundFormUiState> = _uiState

    init {
        viewModelScope.launch {
            combine(
                getFabricRollStreamUseCase(rollId),
                _uiState.map { it.lengthUnit }.distinctUntilChanged(),
                ::Pair
            ).collect { (fabricRoll, lengthUnit) ->
                if (fabricRoll != null) {
                    initUiState(fabricRoll, lengthUnit)
                }
            }
        }
    }

    fun onQtyToProcessChange(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                qtyToProcessFieldState = uiState.qtyToProcessFieldState.copy(value = value)
            )
        }
    }

    fun onLengthUnitChange(lengthUnit: LengthUnit) {
        _uiState.update { uiState ->
            uiState.copy(lengthUnit = lengthUnit)
        }
    }

    fun onBuyerChange(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                buyerFieldState = uiState.buyerFieldState.copy(value = value)
            )
        }
    }

    fun onDateChange(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                dateFieldState = uiState.dateFieldState.copy(value = value)
            )
        }
    }

    fun onRemarkChange(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                remarkFieldState = uiState.remarkFieldState.copy(value = value)
            )
        }
    }

    fun submit() {
        viewModelScope.launch {
            _uiState.update { it.copy(submitInProgress = true) }
            cancellableRunCatching { outboundFabricRoll() }
                .onSuccess { _uiState.update { it.copy(submitInProgress = false) } }
                .onFailure { _uiState.update { it.copy(submitInProgress = false) } }
        }
    }

    fun onSideEffectConsumed() {
        _uiState.update {
            it.copy(sideEffect = null)
        }
    }

    private suspend fun outboundFabricRoll() {
        val dateFormat = uiState.value.dateFieldState.format
        val quantity = uiState.value.qtyToProcessFieldState.value.toDouble()
        val lengthUnit = uiState.value.lengthUnit

        val request = OutboundRequest(
            rollId = rollId,
            qtyToProcess = quantity / lengthUnit.multiplier,
            buyer = uiState.value.buyerFieldState.value,
            date = dateFormat
                .parse(uiState.value.dateFieldState.value)
                .atStartOfDayIn(TimeZone.currentSystemDefault()),
            remark = uiState.value.remarkFieldState.value,
        )
        outboundFabricRollUseCase(request)
    }

    private fun initUiState(
        fabricRoll: FabricRoll,
        lengthUnit: LengthUnit,
    ) {
        _uiState.update { uiState ->
            uiState.copy(
                itemNoFieldState = uiState.itemNoFieldState.copy(value = fabricRoll.itemNo),
                orderNoFieldState = uiState.orderNoFieldState.copy(
                    value = fabricRoll.orderNo.ifEmpty { "-" }
                ),
                availableQtyFieldState = uiState.availableQtyFieldState.copy(
                    value = (fabricRoll.remainingQuantity * lengthUnit.multiplier).formatDecimal(1)
                ),
                qtyToProcessFieldState = uiState.qtyToProcessFieldState.copy(
                    value = "",
                    validators = listOf(
                        Validators.isPositiveNumber(),
                        Validators.isInRange(max = fabricRoll.remainingQuantity / lengthUnit.multiplier)
                    )
                ),
            )
        }
    }
}
