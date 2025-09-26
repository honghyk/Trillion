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
import trillion.wms.core.ui.model.LengthUnit

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
        onSideEffectConsumed = viewModel::onSideEffectConsumed,
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
    onSideEffectConsumed: () -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(uiState.sideEffect) {
        when (uiState.sideEffect) {
            is OutboundFormUiState.SideEffect.Dismiss -> onDismiss()
            null -> {}
        }
        onSideEffectConsumed()
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
                    loading = uiState.submitInProgress,
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
            value = uiState.itemNoFieldState.value,
            enabled = false,
            onValueChange = {},
        )
        FormTextField(
            label = "Order No",
            value = uiState.orderNoFieldState.value,
            enabled = false,
            onValueChange = {},
        )
        FormTextField(
            label = "현재 수량",
            value = uiState.availableQtyFieldState.value + when (uiState.lengthUnit) {
                LengthUnit.METER -> " m"
                LengthUnit.YARD -> " yd"
            },
            enabled = false,
            onValueChange = {},
        )
        QuantityFormField(
            quantityFieldState = uiState.qtyToProcessFieldState,
            lengthUnit = uiState.lengthUnit,
            onValueChange = onQtyToProcessChange,
            onLengthUnitSelected = onLengthUnitChange,
        )
        FormTextField(
            label = "Buyer *",
            placeholder = "바이어를 입력하세요",
            value = uiState.buyerFieldState.value,
            onValueChange = onBuyerChange,
        )
        FormDateField(
            label = "Date *",
            value = uiState.dateFieldState.value,
            placeholder = "2025-01-01",
            isError = uiState.dateFieldState.isError,
            supportingText = {
                uiState.dateFieldState.errorMessage?.let {
                    Text(text = it)
                }
            },
            onValueChange = onDateChange,
        )
        FormTextField(
            label = "비고 (선택사항)",
            placeholder = "비고를 입력하세요",
            value = uiState.remarkFieldState.value,
            onValueChange = onRemarkChange,
            singleLine = false,
            modifier = Modifier.height(120.dp),
        )
    }
}
