package trillion.wms.feature.fabricroll.form

import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import trillion.wms.core.designsystem.component.FormButtonState
import trillion.wms.core.designsystem.component.FormDialog
import trillion.wms.core.designsystem.component.FormDialogTitle
import trillion.wms.core.designsystem.component.FormDropDownField
import trillion.wms.core.designsystem.component.FormHorizontalTwoButton
import trillion.wms.core.designsystem.component.FormNumberTextField
import trillion.wms.core.designsystem.component.FormTextField
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.component.QuantityFormField
import trillion.wms.core.ui.model.LengthUnit

@Composable
fun FabricRollFormDialog(
    zoneId: Long,
    rollId: Long?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FabricRollFormViewModel = koinViewModel { parametersOf(zoneId, rollId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FabricRollFormDialog(
        uiState = uiState,
        onDismiss = onDismiss,
        onSubmit = viewModel::submit,
        onSideEffectConsumed = viewModel::onSideEffectConsumed,
        onZoneChange = viewModel::updateSelectedZone,
        onRollIdChange = viewModel::updateRollId,
        onItemNoChange = viewModel::updateItemNo,
        onOrderNoChange = viewModel::updateOrderNo,
        onColorChange = viewModel::updateColor,
        onFactoryChange = viewModel::updateFactory,
        onFinishChange = viewModel::updateFinish,
        onQuantityChange = viewModel::updateQuantity,
        onLengthUnitChange = viewModel::updateLengthUnit,
        onRemarkChange = viewModel::updateRemark,
        modifier = modifier,
    )
}

@Composable
private fun FabricRollFormDialog(
    uiState: FabricRollFormUiState,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    onSideEffectConsumed: () -> Unit,
    onZoneChange: (Zone) -> Unit,
    onRollIdChange: (String) -> Unit,
    onItemNoChange: (String) -> Unit,
    onOrderNoChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onFactoryChange: (String) -> Unit,
    onFinishChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onLengthUnitChange: (LengthUnit) -> Unit,
    onRemarkChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.sideEffect) {
        when (uiState.sideEffect) {
            is FabricRollFormUiState.SideEffect.Dismiss -> onDismiss()
            is FabricRollFormUiState.SideEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(uiState.sideEffect.message)
            }

            null -> {}
        }
        onSideEffectConsumed()
    }

    FormDialog(
        modifier = modifier,
        title = {
            FormDialogTitle(
                title = if (uiState.isEdit) "롤 수정" else "롤 추가",
                onDismiss = onDismiss,
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
                ),
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            FormDropDownField(
                label = "구역",
                value = uiState.selectedZone?.name ?: "",
                onOptionSelected = { index -> onZoneChange(uiState.zones[index]) },
                options = uiState.zones.map { it.name },
                placeholder = "구역을 선택하세요",
            )
            FormNumberTextField(
                label = "No *",
                value = uiState.rollIdFieldState.value,
                onValueChange = onRollIdChange,
                enabled = !uiState.isEdit,
                isError = uiState.rollIdFieldState.isError,
                supportingText = { uiState.rollIdFieldState.errorMessage?.let { Text(it) } },
                placeholder = "번호를 입력하세요"
            )
            FormTextField(
                label = "Item No",
                value = uiState.itemNoFieldState.value,
                onValueChange = onItemNoChange,
                isError = uiState.itemNoFieldState.isError,
                supportingText = { uiState.itemNoFieldState.errorMessage?.let { Text(it) } },
                placeholder = "품목 번호를 입력하세요"
            )
            QuantityFormField(
                quantityFieldState = uiState.quantityFieldState,
                lengthUnit = uiState.lengthUnit,
                enabled = !uiState.isEdit,
                onValueChange = onQuantityChange,
                onLengthUnitSelected = onLengthUnitChange,
            )
            FormTextField(
                label = "Order No",
                value = uiState.orderNoFieldState.value,
                onValueChange = onOrderNoChange,
                placeholder = "주문 번호를 입력하세요"
            )
            FormTextField(
                label = "Color",
                value = uiState.colorFieldState.value,
                onValueChange = onColorChange,
                placeholder = "색상을 입력하세요"
            )
            FormTextField(
                label = "Factory",
                value = uiState.factoryFieldState.value,
                onValueChange = onFactoryChange,
                placeholder = "공장을 입력하세요"
            )
            FormTextField(
                label = "Finish",
                value = uiState.finishFieldState.value,
                onValueChange = onFinishChange,
                placeholder = "마감을 입력하세요"
            )
            FormTextField(
                label = "Remark",
                value = uiState.remarkFieldState.value,
                onValueChange = onRemarkChange,
                placeholder = "비고를 입력하세요",
                modifier = Modifier.height(120.dp),
            )
        }
    )
}
