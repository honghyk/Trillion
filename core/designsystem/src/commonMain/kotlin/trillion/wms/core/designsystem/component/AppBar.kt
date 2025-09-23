@file:OptIn(ExperimentalMaterial3Api::class)

package trillion.wms.core.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.designsystem.extensions.Direction
import trillion.wms.core.designsystem.extensions.drawBorder
import trillion.wms.core.designsystem.theme.SdsTheme
import kotlin.math.ceil

@Composable
fun SdsTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable SdsTopAppBarNavScope.() -> Unit = {},
    actions: @Composable SdsTopAppBarActionsScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: SdsTopAppBarColors = SdsTopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    SdsTopAppBarInternal(
        centerAligned = false,
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        expandedHeight = expandedHeight,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun SdsCenterAlignedTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable SdsTopAppBarNavScope.() -> Unit = {},
    actions: @Composable SdsTopAppBarActionsScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: SdsTopAppBarColors = SdsTopAppBarDefaults.centerAlignedTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    SdsTopAppBarInternal(
        centerAligned = true,
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        expandedHeight = expandedHeight,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun SdsTopAppBarInternal(
    centerAligned: Boolean,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable SdsTopAppBarNavScope.() -> Unit = {},
    actions: @Composable SdsTopAppBarActionsScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: SdsTopAppBarColors = SdsTopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val borderModifier = modifier.drawBorder(
        width = 1.dp,
        color = colors.borderColor,
        direction = Direction.BOTTOM
    )
    val wrappedTitle = @Composable {
        ProvideTextStyle(value = SdsTheme.typography.headlineSmall) {
            title()
        }
    }
    val wrappedNavigationIcon: @Composable () -> Unit = {
        SdsTopAppBarNavScopeImpl(colors).navigationIcon()
    }
    val wrappedActions: @Composable RowScope.() -> Unit = {
        SdsTopAppBarActionsScopeImpl(this, colors).actions()
        Spacer(Modifier.width(16.dp))
    }
    val material3Colors = colors.toMaterial3TopAppBarColors()

    if (centerAligned) {
        CenterAlignedTopAppBar(
            title = wrappedTitle,
            modifier = borderModifier,
            navigationIcon = wrappedNavigationIcon,
            actions = wrappedActions,
            expandedHeight = expandedHeight,
            windowInsets = windowInsets,
            colors = material3Colors,
            scrollBehavior = scrollBehavior
        )
    } else {
        TopAppBar(
            title = wrappedTitle,
            modifier = borderModifier,
            navigationIcon = wrappedNavigationIcon,
            actions = wrappedActions,
            expandedHeight = expandedHeight,
            windowInsets = windowInsets,
            colors = material3Colors,
            scrollBehavior = scrollBehavior,
        )
    }
}

object SdsTopAppBarDefaults {

    @Composable
    fun topAppBarColors(): SdsTopAppBarColors = SdsTopAppBarColors(
        containerColor = SdsTheme.colorScheme.backgroundDefaultTertiary,
        scrolledContainerColor = SdsTheme.colorScheme.backgroundDefaultDefault,
        navigationIconContentColor = SdsTheme.colorScheme.iconDefaultDefault,
        titleContentColor = SdsTheme.colorScheme.textDefaultDefault,
        actionIconContentColor = SdsTheme.colorScheme.iconDefaultDefault,
        actionIconDisabledContentColor = SdsTheme.colorScheme.iconDisabled,
        borderColor = SdsTheme.colorScheme.borderDefaultDefault.copy(alpha = 0.4f),
    )

    @Composable
    fun centerAlignedTopAppBarColors(): SdsTopAppBarColors =
        topAppBarColors()
}

@Stable
data class SdsTopAppBarColors(
    val containerColor: Color,
    val scrolledContainerColor: Color,
    val navigationIconContentColor: Color,
    val titleContentColor: Color,
    val actionIconContentColor: Color,
    val actionIconDisabledContentColor: Color,
    val borderColor: Color,
) {

    fun actionIconContentColor(enabled: Boolean): Color =
        if (enabled) actionIconContentColor else actionIconDisabledContentColor

    fun toMaterial3TopAppBarColors() = TopAppBarColors(
        containerColor = containerColor,
        scrolledContainerColor = scrolledContainerColor,
        navigationIconContentColor = navigationIconContentColor,
        titleContentColor = titleContentColor,
        actionIconContentColor = actionIconContentColor,
    )
}

interface SdsTopAppBarNavScope {

    @Composable
    fun BackIcon(
        onClick: () -> Unit,
        enabled: Boolean = true,
    )

    @Composable
    fun CloseIcon(
        onClick: () -> Unit,
        enabled: Boolean = true,
    )
}

interface SdsTopAppBarActionsScope : RowScope {

    @Composable
    fun ActionText(
        modifier: Modifier = Modifier,
        text: String,
        onClick: () -> Unit,
        enabled: Boolean = true,
    )

    @Composable
    fun ActionIcon(
        imageVector: ImageVector,
        contentDescription: String?,
        onClick: () -> Unit,
        enabled: Boolean = true,
    )

    @Composable
    fun RefreshIcon(
        isRefreshing: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    )
}

private class SdsTopAppBarNavScopeImpl(
    private val colors: SdsTopAppBarColors,
) : SdsTopAppBarNavScope {

    @Composable
    override fun BackIcon(
        onClick: () -> Unit,
        enabled: Boolean
    ) {
        SdsIconButton(
            modifier = Modifier.size(48.dp),
            onClick = onClick,
            enabled = enabled,
        ) {
            Icon(
                modifier = Modifier.size(TopAppBarIconSize),
                imageVector = vectorResource(Icons.ArrowBack),
                tint = colors.navigationIconContentColor,
                contentDescription = null,
            )
        }
    }

    @Composable
    override fun CloseIcon(
        onClick: () -> Unit,
        enabled: Boolean
    ) {
        SdsIconButton(
            modifier = Modifier.size(48.dp),
            onClick = onClick,
            enabled = enabled,
        ) {
            Icon(
                modifier = Modifier.size(TopAppBarIconSize),
                imageVector = vectorResource(Icons.ArrowBack),
                tint = colors.navigationIconContentColor,
                contentDescription = null,
            )
        }
    }
}

private class SdsTopAppBarActionsScopeImpl(
    private val rowScope: RowScope,
    private val colors: SdsTopAppBarColors,
) : SdsTopAppBarActionsScope, RowScope by rowScope {

    @Composable
    override fun ActionText(
        modifier: Modifier,
        text: String,
        onClick: () -> Unit,
        enabled: Boolean
    ) {
        Text(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    indication = ripple(),
                    interactionSource = null,
                )
                .padding(8.dp),
            text = text,
            style = SdsTheme.typography.bodyStrong,
            color = colors.actionIconContentColor(enabled),
        )
    }

    @Composable
    override fun ActionIcon(
        imageVector: ImageVector,
        contentDescription: String?,
        onClick: () -> Unit,
        enabled: Boolean,
    ) {
        val color = colors.actionIconContentColor(enabled)
        SdsIconButton(
            modifier = Modifier
                .size(TopAppBarIconButtonSize),
            onClick = onClick,
            enabled = enabled,
            content = {
                Icon(
                    modifier = Modifier.size(TopAppBarIconSize),
                    imageVector = imageVector,
                    tint = color,
                    contentDescription = contentDescription,
                )
            }
        )
    }

    @Composable
    override fun RefreshIcon(
        isRefreshing: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean
    ) {
        val rotation = remember { Animatable(0f) }
        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                rotation.animateTo(
                    targetValue = rotation.value + 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 800)
                    )
                )
            } else {
                val targetRotation = ceil(rotation.value / 360f) * 360f
                rotation.animateTo(
                    targetValue = targetRotation,
                    animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
                )
            }
        }

        SdsIconButton(
            modifier = modifier
                .size(TopAppBarIconButtonSize),
            onClick = onClick,
            enabled = enabled,
            content = {
                Icon(
                    modifier = Modifier
                        .size(TopAppBarIconSize)
                        .graphicsLayer {
                            rotationZ = rotation.value % 360f
                        },
                    imageVector = vectorResource(Icons.RefreshCw),
                    contentDescription = null,
                )
            }
        )
    }
}

private val TopAppBarIconButtonSize = 48.dp
private val TopAppBarIconSize = 20.dp
