package trillion.wms.feature.zone.form

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import trillion.wms.core.designsystem.component.FormDialog
import trillion.wms.core.designsystem.component.FormButtonState
import trillion.wms.core.designsystem.component.FormHorizontalTwoButton
import trillion.wms.core.designsystem.component.FormTextField
import trillion.wms.core.designsystem.component.FormDialogTitle
import trillion.wms.core.designsystem.theme.SdsTheme
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.TextFormFieldState

@Composable
fun ZoneFormDialog(
    zoneId: Long?,
    onDismiss: () -> Unit,
    viewModel: ZoneFormViewModel = koinViewModel { parametersOf(zoneId) },
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ZoneFormDialog(
        uiState = uiState,
        onZoneNameChange = viewModel::updateNameField,
        onZoneDescriptionChange = viewModel::updateDescriptionField,
        onSubmit = viewModel::submit,
        onDismiss = onDismiss,
        modifier = modifier,
    )
}

@Composable
private fun ZoneFormDialog(
    uiState: ZoneFormUiState,
    modifier: Modifier = Modifier,
    onZoneNameChange: (String) -> Unit,
    onZoneDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(uiState.formSubmitState) {
        if (uiState.formSubmitState == FormSubmitState.SUBMITTED) {
            onDismiss()
        }
    }

    FormDialog(
        modifier = modifier,
        title = {
            FormDialogTitle(
                title = if (uiState.isEditMode) "구역 수정" else "구역 생성",
                onDismiss = onDismiss
            )
        },
        action = {
            FormHorizontalTwoButton(
                primaryButtonState = FormButtonState(
                    text = "확인",
                    enabled = uiState.submitEnabled,
                    loading = uiState.formSubmitState == FormSubmitState.IN_PROGRESS,
                    onClick = onSubmit,
                ),
                secondaryButtonState = FormButtonState(
                    text = "취소",
                    onClick = onDismiss,
                ),
            )
        },
    ) {
        FormTextField(
            value = uiState.nameField.value,
            onValueChange = onZoneNameChange,
            label = "이름 *",
            placeholder = "이름을 입력하세요",
            isError = uiState.nameField.isError,
            supportingText = {
                uiState.nameField.errorMessage?.let { errorMessage ->
                    Text(text = errorMessage)
                }
            }
        )
        FormTextField(
            value = uiState.descriptionField.value,
            onValueChange = onZoneDescriptionChange,
            label = "설명 (선택사항)",
            placeholder = "설명을 입력하세요",
            isError = uiState.descriptionField.isError,
            singleLine = false,
            modifier = Modifier.height(120.dp),
        )
    }
}

@Composable
@Preview
private fun ZoneFormScreenPreview() {
    SdsTheme {
        ZoneFormDialog(
            uiState = ZoneFormUiState(
                isEditMode = false,
                nameField = TextFormFieldState(value = "A-1"),
                descriptionField = TextFormFieldState(value = ""),
            ),
            onZoneNameChange = {},
            onZoneDescriptionChange = {},
            onSubmit = {},
            onDismiss = {},
        )
    }
}
