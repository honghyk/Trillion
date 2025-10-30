package trillion.wms.feature.zone.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import trillion.wms.core.domain.CreateZoneUseCase
import trillion.wms.core.domain.GetZoneStreamUseCase
import trillion.wms.core.domain.UpdateZoneUseCase
import trillion.wms.core.model.CreateZoneRequest
import trillion.wms.core.model.UpdateZoneRequest
import trillion.wms.core.model.exception.AlreadyExistsException
import trillion.wms.core.ui.model.FormSubmitState

class ZoneFormViewModel(
    private val zoneId: Long?,
    private val getZoneStream: GetZoneStreamUseCase,
    private val createZone: CreateZoneUseCase,
    private val updateZone: UpdateZoneUseCase,
) : ViewModel() {
    private val isEditMode = zoneId != null

    private val _uiState = MutableStateFlow(ZoneFormUiState(isEditMode = isEditMode))
    val uiState: StateFlow<ZoneFormUiState> = _uiState.asStateFlow()

    init {
        if (zoneId != null) {
            loadZone(zoneId)
        }
    }

    private fun loadZone(zoneId: Long) {
        viewModelScope.launch {
            getZoneStream(GetZoneStreamUseCase.Params.ZoneId(zoneId))
                .collectLatest {
                    updateNameField(it?.name.orEmpty())
                    updateDescriptionField(it?.description.orEmpty())
                }
        }
    }

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
        if (currentState.formSubmitState == FormSubmitState.IN_PROGRESS) return

        viewModelScope.launch {
            _uiState.update { it.copy(formSubmitState = FormSubmitState.IN_PROGRESS) }

            val result = if (isEditMode) {
                val request = UpdateZoneRequest(
                    id = zoneId!!,
                    name = currentState.nameField.value,
                    description = currentState.descriptionField.value,
                )
                updateZone(UpdateZoneUseCase.Params(request))
            } else {
                val request = CreateZoneRequest(
                    name = currentState.nameField.value,
                    description = currentState.descriptionField.value,
                )
                createZone(CreateZoneUseCase.Params(request))
            }

            result
                .fold(
                    onSuccess = { handleSuccessfulSubmit() },
                    onFailure = { handleSubmitFailure(it) }
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
