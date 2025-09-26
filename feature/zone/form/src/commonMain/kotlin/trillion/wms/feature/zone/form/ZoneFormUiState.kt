package trillion.wms.feature.zone.form

import trillion.wms.core.ui.model.TextFormFieldState

data class ZoneFormUiState(
    val nameFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val descriptionFieldState: TextFormFieldState = TextFormFieldState(value = ""),
    val submitInProgress: Boolean = false,
    val sideEffect: SideEffect? = null,
) {
    val submitEnabled: Boolean
        get() = nameFieldState.value.isNotEmpty()

    sealed interface SideEffect {
        data object Dismiss : SideEffect
    }
}
