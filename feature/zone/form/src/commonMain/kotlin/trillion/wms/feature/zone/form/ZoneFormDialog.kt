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
import trillion.wms.core.designsystem.component.FormDialog
import trillion.wms.core.designsystem.component.FormButtonState
import trillion.wms.core.designsystem.component.FormHorizontalTwoButton
import trillion.wms.core.designsystem.component.FormTextField
import trillion.wms.core.designsystem.component.FormDialogTitle
import trillion.wms.core.designsystem.theme.SdsTheme
import trillion.wms.core.ui.model.TextFormFieldState

@Composable
fun ZoneFormDialog(
    onDismiss: () -> Unit,
    viewModel: ZoneFormViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ZoneFormDialog(
        uiState = uiState,
        onZoneNameChange = viewModel::updateNameField,
        onZoneDescriptionChange = viewModel::updateDescriptionField,
        onSideEffectConsumed = viewModel::onSideEffectConsumed,
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
    onSideEffectConsumed: () -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(uiState.sideEffect) {
        when (uiState.sideEffect) {
            is ZoneFormUiState.SideEffect.Dismiss -> onDismiss()
            null -> {}
        }
        onSideEffectConsumed()
    }

    FormDialog(
        modifier = modifier,
        title = {
            FormDialogTitle(
                title = "구역 생성",
                onDismiss = onDismiss
            )
        },
        action = {
            FormHorizontalTwoButton(
                primaryButtonState = FormButtonState(
                    text = "구역 생성",
                    enabled = uiState.submitEnabled,
                    loading = uiState.submitInProgress,
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
            value = uiState.nameFieldState.value,
            onValueChange = onZoneNameChange,
            label = "이름 *",
            placeholder = "이름을 입력하세요",
            isError = uiState.nameFieldState.isError,
            supportingText = {
                uiState.nameFieldState.errorMessage?.let { errorMessage ->
                    Text(text = errorMessage)
                }
            }
        )
        FormTextField(
            value = uiState.descriptionFieldState.value,
            onValueChange = onZoneDescriptionChange,
            label = "설명 (선택사항)",
            placeholder = "설명을 입력하세요",
            isError = uiState.descriptionFieldState.isError,
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
                nameFieldState = TextFormFieldState(value = "A-1"),
                descriptionFieldState = TextFormFieldState(value = ""),
            ),
            onZoneNameChange = {},
            onZoneDescriptionChange = {},
            onSideEffectConsumed = {},
            onSubmit = {},
            onDismiss = {},
        )
    }
}
