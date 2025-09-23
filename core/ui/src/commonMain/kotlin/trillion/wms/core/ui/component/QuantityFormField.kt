package trillion.wms.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.unit.dp
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.model.NumberFieldState
import trillion.wms.core.designsystem.component.FormNumberTextField

@Composable
fun QuantityFormField(
    quantityFieldState: NumberFieldState,
    lengthUnit: LengthUnit,
    onValueChange: (String) -> Unit,
    onLengthUnitSelected: (LengthUnit) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FormNumberTextField(
            label = "수량",
            value = quantityFieldState.value,
            placeholder = "수량을 입력하세요",
            enabled = enabled,
            isError = quantityFieldState.isError,
            supportingText = {
                quantityFieldState.errorMessage?.let { errorMessage ->
                    Text(text = errorMessage)
                }
            },
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f).alignBy(LastBaseline),
        )
        LengthUnitToggleButtons(
            modifier = Modifier
                .alignByBaseline(),
            selected = lengthUnit,
            onSelected = onLengthUnitSelected,
        )
    }
}
