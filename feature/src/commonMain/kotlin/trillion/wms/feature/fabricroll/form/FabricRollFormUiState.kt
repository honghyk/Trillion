package trillion.wms.feature.fabricroll.form

import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.model.NumberFieldState
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.TextFormFieldState
import trillion.wms.core.ui.model.Validators
import trillion.wms.core.model.Zone

data class FabricRollFormUiState(
    val isEdit: Boolean,
    val zones: List<Zone> = emptyList(),
    val selectedZone: Zone? = null,
    val rollIdFieldState: NumberFieldState = NumberFieldState(
        value = "",
        validators = listOf(Validators.isPositiveNumber())
    ),
    val itemNoFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val orderNoFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val colorFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val factoryFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val finishFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val remarkFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val quantityFieldState: NumberFieldState = NumberFieldState(
        value = "",
        validators = listOf(Validators.isPositiveNumber())
    ),
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val submitInProgress: Boolean = false,
    val sideEffect: SideEffect? = null,
) {
    val submitEnabled: Boolean
        get() = selectedZone != null &&
                rollIdFieldState.value.isNotBlank() && !rollIdFieldState.isError &&
                quantityFieldState.value.isNotBlank() && !quantityFieldState.isError

    sealed interface SideEffect {
        object Dismiss : SideEffect
        data class ShowSnackbar(val message: String) : SideEffect
    }
}
