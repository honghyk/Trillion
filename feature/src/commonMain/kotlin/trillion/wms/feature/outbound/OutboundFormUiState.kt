package trillion.wms.feature.outbound

import trillion.wms.core.ui.model.DateFieldState
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.model.NumberFieldState
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.TextFormFieldState
import trillion.wms.core.ui.utils.InstantFormatter
import trillion.wms.feature.fabricroll.form.FabricRollFormUiState
import kotlin.time.Clock

data class OutboundFormUiState(
    val itemNoFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val orderNoFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val availableQtyFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val qtyToProcessFieldState: NumberFieldState = NumberFieldState(value = ""),
    val buyerFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val dateFieldState: DateFieldState = DateFieldState(
        value = InstantFormatter.formatBasicDate(Clock.System.now())
    ),
    val remarkFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val submitInProgress: Boolean = false,
    val sideEffect: SideEffect? = null,
) {
    val submitEnabled: Boolean
        get() = qtyToProcessFieldState.value.isNotEmpty()
                && !qtyToProcessFieldState.isError
                && buyerFieldState.value.isNotEmpty()

    sealed interface SideEffect {
        data object Dismiss : SideEffect
    }
}
