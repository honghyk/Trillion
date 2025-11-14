package trillion.wms.feature.outbound

import androidx.compose.runtime.Stable
import trillion.wms.core.ui.model.DateFieldState
import trillion.wms.core.ui.model.FormFieldState
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.model.NumberFieldState
import trillion.wms.core.ui.model.TextFormFieldState
import trillion.wms.core.ui.model.Validator
import trillion.wms.core.ui.model.Validators
import trillion.wms.core.ui.utils.InstantFormatter
import trillion.wms.core.ui.utils.formatDecimal
import kotlin.time.Clock

@Stable
data class OutboundFormUiState(
    val itemNo: String = "",
    val orderNo: String = "",
    val availableQtyInMeters: Double = 0.0,
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val quantityToProcess: String = "",
    val buyerField: TextFormFieldState = TextFormFieldState(value = ""),
    val dateField: DateFieldState = DateFieldState(
        value = InstantFormatter.formatBasicDate(Clock.System.now())
    ),
    val remarkField: TextFormFieldState = TextFormFieldState(value = ""),
    val formSubmitState: FormSubmitState = FormSubmitState.IDLE,
) {

    val qtyToProcessField: NumberFieldState = NumberFieldState(
        value = quantityToProcess,
        validators = listOf(
            Validators.isPositiveNumber(),
            Validators.isInRange(max = availableQtyInMeters * lengthUnit.multiplier)
        )
    )

    val availableQtyInCurrentUnit: Double
        get() = availableQtyInMeters * lengthUnit.multiplier

    val submitEnabled: Boolean
        get() = qtyToProcessField.value.isNotEmpty() && !qtyToProcessField.isError
                && buyerField.value.isNotEmpty()
}
