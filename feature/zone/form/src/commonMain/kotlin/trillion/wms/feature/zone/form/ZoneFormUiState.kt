package trillion.wms.feature.zone.form

import androidx.compose.runtime.Stable
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.TextFormFieldState

@Stable
data class ZoneFormUiState(
    val isEditMode: Boolean,
    val nameField: TextFormFieldState = TextFormFieldState(value = ""),
    val descriptionField: TextFormFieldState = TextFormFieldState(value = ""),
    val formSubmitState: FormSubmitState = FormSubmitState.IDLE,
) {
    val submitEnabled: Boolean
        get() = nameField.value.isNotEmpty()
}
