package trillion.wms.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.extensions.Direction
import trillion.wms.core.designsystem.extensions.drawBorder
import trillion.wms.core.designsystem.theme.SdsTheme
import trillion.wms.core.designsystem.theme.contentColorFor

@Composable
fun SdsNavigationRail(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val containerColor = SdsNavigationRailDefaults.containerColor
    Surface(
        color = containerColor,
        contentColor = contentColorFor(containerColor),
        modifier = modifier,
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .windowInsetsPadding(SdsNavigationRailDefaults.windowInsets)
                .selectableGroup()
                .drawBorder(
                    width = 1.dp,
                    color = SdsNavigationRailDefaults.borderColor,
                    direction = Direction.RIGHT,
                )
                .padding(NavigationRailPadding)
                .width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(NavigationRailItemSpacing)
        ) {
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun SdsNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = SdsNavigationRailItemDefaults.colors()
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val styledIcon = @Composable {
        val iconColor = colors.iconColor(selected = selected, enabled = enabled)
        Box(modifier = Modifier) {
            CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
        }
    }

    val styledLabel = label?.let {
        @Composable {
            val style = SdsTheme.typography.bodySmall
            val textColor = colors.textColor(selected = selected, enabled = enabled)
            CompositionLocalProvider(
                LocalContentColor provides textColor,
                LocalTextStyle provides style,
                content = label
            )
        }
    }

    Box(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = ripple(),
            )
            .defaultMinSize(minHeight = NavigationRailItemHeight)
            .widthIn(min = NavigationRailItemWidth),
        contentAlignment = Alignment.Center,
        propagateMinConstraints = true,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            styledIcon()
            if (styledLabel != null) {
                Spacer(Modifier.height(8.dp))
                styledLabel()
            }
        }
    }
}

object SdsNavigationRailDefaults {

    val containerColor: Color
        @Composable get() = SdsTheme.colorScheme.backgroundDefaultTertiary

    val borderColor: Color
        @Composable get() = SdsTheme.colorScheme.borderDefaultDefault.copy(alpha = 0.4f)

    val windowInsets: WindowInsets
        @Composable get() = NavigationRailDefaults.windowInsets
}

object SdsNavigationRailItemDefaults {

    @Composable
    fun colors(): NavigationRailItemColors = NavigationRailItemColors(
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
internal fun NavigationRailItemColors.iconColor(selected: Boolean, enabled: Boolean): Color =
    when {
        !enabled -> disabledIconColor
        selected -> selectedIconColor
        else -> unselectedIconColor
    }

@Stable
internal fun NavigationRailItemColors.textColor(selected: Boolean, enabled: Boolean): Color =
    when {
        !enabled -> disabledTextColor
        selected -> selectedTextColor
        else -> unselectedTextColor
    }

private val NavigationRailPadding = 8.dp

private val NavigationRailHeaderPadding = 8.dp

private val NavigationRailItemSpacing = 4.dp

private val NavigationRailItemHeight = 48.dp

private val NavigationRailItemWidth = 48.dp
