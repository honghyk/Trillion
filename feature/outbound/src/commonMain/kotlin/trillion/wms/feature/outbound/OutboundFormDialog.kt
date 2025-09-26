package trillion.wms.feature.outbound

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import trillion.wms.core.designsystem.component.FormButtonState
import trillion.wms.core.designsystem.component.FormDateField
import trillion.wms.core.designsystem.component.FormDialog
import trillion.wms.core.designsystem.component.FormDialogTitle
import trillion.wms.core.designsystem.component.FormHorizontalTwoButton
import trillion.wms.core.designsystem.component.FormTextField
import trillion.wms.core.ui.component.QuantityFormField
import trillion.wms.core.ui.model.FormSubmitState
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.formatDecimal

@Composable
fun OutboundFormDialog(
    rollId: Long,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OutboundFormViewModel = koinViewModel { parametersOf(rollId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    OutboundFormScreen(
        uiState = uiState,
        onQtyToProcessChange = viewModel::onQtyToProcessChange,
        onLengthUnitChange = viewModel::onLengthUnitChange,
        onBuyerChange = viewModel::onBuyerChange,
        onDateChange = viewModel::onDateChange,
        onRemarkChange = viewModel::onRemarkChange,
        onSubmit = viewModel::submit,
        onDismiss = onDismiss,
        modifier = modifier,
    )
}

@Composable
private fun OutboundFormScreen(
    uiState: OutboundFormUiState,
    onQtyToProcessChange: (String) -> Unit,
    onLengthUnitChange: (LengthUnit) -> Unit,
    onBuyerChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onRemarkChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
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
                title = "출고 처리",
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
                )
            )
        }
    ) {
        FormTextField(
            label = "Item No",
            value = uiState.itemNo,
            enabled = false,
            onValueChange = {},
        )
        FormTextField(
            label = "Order No",
            value = uiState.orderNo,
            enabled = false,
            onValueChange = {},
        )
        FormTextField(
            label = "현재 수량",
            value = uiState.availableQtyInCurrentUnit.formatDecimal(1) + when (uiState.lengthUnit) {
                LengthUnit.METER -> " m"
                LengthUnit.YARD -> " yd"
            },
            enabled = false,
            onValueChange = {},
        )
        QuantityFormField(
            quantityFieldState = uiState.qtyToProcessField,
            lengthUnit = uiState.lengthUnit,
            onValueChange = onQtyToProcessChange,
            onLengthUnitSelected = onLengthUnitChange,
        )
        FormTextField(
            label = "Buyer *",
            placeholder = "바이어를 입력하세요",
            value = uiState.buyerField.value,
            onValueChange = onBuyerChange,
        )
        FormDateField(
            label = "Date *",
            value = uiState.dateField.value,
            placeholder = "2025-01-01",
            isError = uiState.dateField.isError,
            supportingText = {
                uiState.dateField.errorMessage?.let {
                    Text(text = it)
                }
            },
            onValueChange = onDateChange,
        )
        FormTextField(
            label = "비고 (선택사항)",
            placeholder = "비고를 입력하세요",
            value = uiState.remarkField.value,
            onValueChange = onRemarkChange,
            singleLine = false,
            modifier = Modifier.height(120.dp),
        )
    }
}
