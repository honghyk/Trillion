package trillion.wms.feature.zone.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import trillion.wms.core.domain.CreateZoneUseCase
import trillion.wms.core.model.CreateZoneRequest
import trillion.wms.core.model.exception.AlreadyExistsException
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.utils.cancellableRunCatching

class ZoneFormViewModel(
    private val createZoneUseCase: CreateZoneUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ZoneFormUiState())
    val uiState: StateFlow<ZoneFormUiState> = _uiState.asStateFlow()

    fun updateNameField(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                nameField = uiState.nameField.copy(
                    value = value,
                    serverErrorMessage = null
                ),
            )
        }
    }

    fun updateDescriptionField(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                descriptionField = uiState.descriptionField.copy(value = value)
            )
        }
    }

    fun submit() {
        val currentState = _uiState.value
        if (currentState.formSubmitState == FormSubmitState.IN_PROGRESS) {
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(formSubmitState = FormSubmitState.IN_PROGRESS) }

            val request = CreateZoneRequest(
                name = currentState.nameField.value,
                description = currentState.descriptionField.value,
            )
            createZoneUseCase(request)
                .fold(
                    onSuccess = { handleSuccessfulSubmit() },
                    onFailure = { e -> handleSubmitFailure(e) }
                )
        }
    }

    private fun handleSuccessfulSubmit() {
        _uiState.update { uiState ->
            uiState.copy(formSubmitState = FormSubmitState.SUBMITTED)
        }
    }

    private fun handleSubmitFailure(e: Throwable) {
        _uiState.update { uiState ->
            uiState.copy(
                nameField = uiState.nameField.copy(serverErrorMessage = e.getErrorMessage()),
                formSubmitState = FormSubmitState.IDLE
            )
        }
    }

    private fun Throwable.getErrorMessage(): String? {
        return when (this) {
            is AlreadyExistsException -> "같은 이름 구역이 존재합니다"
            else -> null
        }
    }
}
