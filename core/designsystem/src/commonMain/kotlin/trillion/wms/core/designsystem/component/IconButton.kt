package trillion.wms.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun SdsIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = SdsTheme.colorScheme.backgroundDefaultDefault,
        contentColor = SdsTheme.colorScheme.iconDefaultDefault,
        disabledContainerColor = SdsTheme.colorScheme.backgroundDisabled,
        disabledContentColor = SdsTheme.colorScheme.iconDisabled,
    ),
    content: @Composable () -> Unit,
) {
    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = containerColor)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                role = Role.Button,
                indication = ripple(),
                interactionSource = interactionSource,
            ),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}
