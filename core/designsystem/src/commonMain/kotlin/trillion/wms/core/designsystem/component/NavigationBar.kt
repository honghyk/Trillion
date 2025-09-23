package trillion.wms.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.extensions.Direction
import trillion.wms.core.designsystem.extensions.drawBorder
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun SdsNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = SdsNavigationBarDefaults.containerColor,
    contentColor: Color = SdsTheme.colorScheme.onBackgroundDefaultDefault,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier
            .drawBorder(
                direction = Direction.TOP,
                width = 1.dp,
                color = SdsNavigationBarDefaults.borderColor
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(NavigationBarItemHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun RowScope.SdsNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: NavigationBarItemColors = SdsNavigationBarItemDefaults.colors(),
    interactionSource: MutableInteractionSource? = null
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val styledIcon = @Composable {
        val iconColor = colors.iconColor(selected = selected, enabled = enabled)
        Box(modifier = Modifier) {
            CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
        }
    }

    val styledLabel = @Composable {
        val style = SdsTheme.typography.bodySmall
        val textColor = colors.textColor(selected = selected, enabled = enabled)
        CompositionLocalProvider(
            LocalContentColor provides textColor,
            LocalTextStyle provides style,
            content = label
        )
    }

    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
            )
            .weight(1f)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center,
        propagateMinConstraints = true,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            styledIcon()
            Spacer(Modifier.height(4.dp))
            styledLabel()
        }
    }
}

object SdsNavigationBarDefaults {

    val containerColor: Color
        @Composable get() = SdsTheme.colorScheme.backgroundDefaultDefault

    val borderColor: Color
        @Composable get() = SdsTheme.colorScheme.borderDefaultDefault.copy(alpha = 0.4f)

    val windowInsets: WindowInsets
        @Composable get() = NavigationBarDefaults.windowInsets
}

object SdsNavigationBarItemDefaults {

    @Composable
    fun colors(): NavigationBarItemColors = NavigationBarItemColors(
        selectedIconColor = SdsTheme.colorScheme.iconBrandDefault,
        selectedTextColor = SdsTheme.colorScheme.textBrandDefault,
        selectedIndicatorColor = SdsTheme.colorScheme.backgroundBrandTertiary,
        unselectedIconColor = SdsTheme.colorScheme.iconBrandDefault,
        unselectedTextColor = SdsTheme.colorScheme.textBrandDefault,
        disabledIconColor = SdsTheme.colorScheme.iconDisabled,
        disabledTextColor = SdsTheme.colorScheme.textDisabled,
    )
}

@Stable
internal fun NavigationBarItemColors.iconColor(selected: Boolean, enabled: Boolean): Color =
    when {
        !enabled -> disabledIconColor
        selected -> selectedIconColor
        else -> unselectedIconColor
    }

@Stable
internal fun NavigationBarItemColors.textColor(selected: Boolean, enabled: Boolean): Color =
    when {
        !enabled -> disabledTextColor
        selected -> selectedTextColor
        else -> unselectedTextColor
    }

private val NavigationBarItemHorizontalPadding = 8.dp
