package trillion.wms.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun SdsRadioGroup(
    modifier: Modifier = Modifier,
    containerColor: Color = SdsTheme.colorScheme.backgroundDefaultSecondary,
    shape: Shape = RoundedCornerShape(8.dp),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .background(
                color = containerColor,
                shape = shape
            )
            .padding(4.dp)
            .selectableGroup()
            .focusProperties { canFocus = false },
        content = content,
    )
}

@Composable
fun SdsRadioButton(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val backgroundColor = buttonBackgroundColor(enabled, selected)
    val contentColor = buttonContentColor(enabled, selected)

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null,
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = contentColor,
            maxLines = 1,
        )
    }
}

@Composable
private fun buttonBackgroundColor(enabled: Boolean, selected: Boolean) = when {
    !enabled -> SdsTheme.colorScheme.backgroundDisabled
    selected -> SdsTheme.colorScheme.backgroundDefaultDefault
    else -> Color.Transparent
}

@Composable
private fun buttonContentColor(enabled: Boolean, selected: Boolean) = when {
    !enabled -> SdsTheme.colorScheme.textDisabled
    selected -> SdsTheme.colorScheme.onBackgroundDefaultDefault
    else -> SdsTheme.colorScheme.onBackgroundDefaultSecondary
}
