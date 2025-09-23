package trillion.wms.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.extensions.Direction
import trillion.wms.core.designsystem.extensions.drawBorder
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun SdsPermanentDrawerSheet(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val containerColor = SdsDrawerDefaults.containerColor
    val borderColor = SdsDrawerDefaults.borderColor
    PermanentDrawerSheet(
        modifier = modifier
            .widthIn(max = 240.dp)
            .drawBorder(
                width = 1.dp,
                color = borderColor,
                direction = Direction.RIGHT
            ),
        drawerShape = RectangleShape,
        drawerContainerColor = containerColor,
        drawerContentColor = contentColorFor(containerColor),
        drawerTonalElevation = 0.dp,
        windowInsets = SdsDrawerDefaults.windowInsets,
        content = { content() }
    )
}

@Composable
fun SdsNavigationDrawerItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null
) {
    val colors = SdsNavigationDrawerItemDefaults.colors()
    Surface(
        selected = selected,
        onClick = onClick,
        modifier =
            modifier
                .semantics { role = Role.Tab }
                .heightIn(min = NavigationDrawerItemHeight)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        color = colors.containerColor(selected).value,
        interactionSource = interactionSource,
    ) {
        Row(
            Modifier.padding(start = 16.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconColor = colors.iconColor(selected).value
            CompositionLocalProvider(
                LocalContentColor provides iconColor,
                content = icon
            )

            Spacer(Modifier.width(12.dp))

            Box(Modifier.weight(1f)) {
                val labelColor = colors.textColor(selected).value
                CompositionLocalProvider(
                    LocalContentColor provides labelColor,
                    LocalTextStyle provides SdsTheme.typography.bodySmall,
                    content = label
                )
            }
        }
    }
}

object SdsDrawerDefaults {

    val containerColor: Color
        @Composable get() = SdsTheme.colorScheme.backgroundDefaultTertiary

    val borderColor: Color
        @Composable get() = SdsTheme.colorScheme.borderDefaultDefault.copy(alpha = 0.4f)

    val windowInsets: WindowInsets
        @Composable get() = DrawerDefaults.windowInsets
}

object SdsNavigationDrawerItemDefaults {

    @Composable
    fun colors(): NavigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = SdsTheme.colorScheme.backgroundDefaultTertiaryHover,
        unselectedContainerColor = Color.Transparent,
        selectedIconColor = SdsTheme.colorScheme.iconDefaultDefault,
        unselectedIconColor = SdsTheme.colorScheme.iconDefaultDefault,
        selectedTextColor = SdsTheme.colorScheme.textDefaultDefault,
        unselectedTextColor = SdsTheme.colorScheme.textDefaultDefault,
    )
}

private val NavigationDrawerItemHeight = 56.dp
