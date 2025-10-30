package trillion.wms.feature.fabricroll.form

import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.model.NumberFieldState
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.TextFormFieldState
import trillion.wms.core.ui.model.Validators
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.model.DateFieldState
import trillion.wms.core.ui.utils.InstantFormatter
import kotlin.time.Clock

data class FabricRollFormUiState(
    val isInEditMode: Boolean,
    val zones: List<Zone> = emptyList(),
    val selectedZone: Zone? = null,
    val rollIdField: NumberFieldState = NumberFieldState(
        value = "",
        validators = listOf(Validators.isPositiveNumber())
    ),
    val itemNoField: TextFormFieldState = TextFormFieldState(value = ""),
    val orderNoField: TextFormFieldState = TextFormFieldState(value = ""),
    val colorField: TextFormFieldState = TextFormFieldState(value = ""),
    val factoryField: TextFormFieldState = TextFormFieldState(value = ""),
    val finishField: TextFormFieldState = TextFormFieldState(value = ""),
    val remarkField: TextFormFieldState = TextFormFieldState(value = ""),
    val quantityField: NumberFieldState = NumberFieldState(
        value = "",
        validators = listOf(Validators.isPositiveNumber())
    ),
    val inboundDateField: DateFieldState = DateFieldState(
        value = InstantFormatter.formatBasicDate(Clock.System.now())
    ),
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val formSubmitState: FormSubmitState = FormSubmitState.IDLE,
) {
    val submitEnabled: Boolean
        get() = selectedZone != null &&
                rollIdField.value.isNotBlank() && !rollIdField.isError &&
                quantityField.value.isNotBlank() && !quantityField.isError
}
