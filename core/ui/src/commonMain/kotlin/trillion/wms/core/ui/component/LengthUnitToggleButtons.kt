package trillion.wms.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.designsystem.component.SdsRadioButton
import trillion.wms.core.designsystem.component.SdsRadioGroup
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun LengthUnitToggleButtons(
    selected: LengthUnit,
    onSelected: (LengthUnit) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsRadioGroup(modifier = modifier) {
        LengthUnit.entries.forEach { lengthUnit ->
            SdsRadioButton(
                selected = selected == lengthUnit,
                label = lengthUnit.unitName,
                onClick = { onSelected(lengthUnit) },
            )
        }
    }
}

@Composable
@Preview
private fun LengthUnitToggleButtonPreview() {
    SdsTheme {
        LengthUnitToggleButtons(
            selected = LengthUnit.YARD,
            onSelected = { },
        )
    }
}
