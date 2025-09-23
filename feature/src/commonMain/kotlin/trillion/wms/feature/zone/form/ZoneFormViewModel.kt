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
                nameFieldState = uiState.nameFieldState.copy(
                    value = value,
                    serverErrorMessage = null
                ),
            )
        }
    }

    fun updateDescriptionField(value: String) {
        _uiState.update { uiState ->
            uiState.copy(
                descriptionFieldState = uiState.descriptionFieldState.copy(value = value)
            )
        }
    }

    fun submit() {
        viewModelScope.launch {
            _uiState.update { uiState -> uiState.copy(submitInProgress = true) }
            createZone()
                .onSuccess {
                    _uiState.update { uiState ->
                        uiState.copy(
                            submitInProgress = false,
                            sideEffect = ZoneFormUiState.SideEffect.Dismiss,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { uiState ->
                        uiState.copy(
                            nameFieldState = uiState.nameFieldState.copy(
                                serverErrorMessage = when (e) {
                                    is AlreadyExistsException -> "같은 이름 구역이 존재합니다"
                                    else -> null
                                }
                            ),
                            submitInProgress = false,
                        )
                    }
                }
        }
    }

    fun onSideEffectConsumed() {
        _uiState.update { uiState -> uiState.copy(sideEffect = null) }
    }

    private suspend fun createZone(): Result<Unit> = cancellableRunCatching {
        createZoneUseCase(
            request = CreateZoneRequest(
                name = uiState.value.nameFieldState.value,
                description = uiState.value.descriptionFieldState.value,
            )
        )
    }
}
