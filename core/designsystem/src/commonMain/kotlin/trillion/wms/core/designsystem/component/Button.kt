package trillion.wms.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.theme.SdsTheme

enum class ButtonVariant {
    Primary, Neutral, Subtle, Danger
}

enum class ButtonSize {
    Small, Medium
}

@Composable
fun SdsButton(
    buttonVariant: ButtonVariant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    shape: Shape = SdsButtonDefaults.shape,
    buttonSize: ButtonSize = ButtonSize.Medium,
    interactionSource: MutableInteractionSource? = null,
) {
    SdsButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = SdsButtonDefaults.buttonColors(buttonVariant),
        border = SdsButtonDefaults.buttonBorder(buttonVariant, enabled),
        style = SdsButtonDefaults.buttonTextStyle(buttonSize),
        contentPadding = SdsButtonDefaults.contentPadding(buttonSize),
        interactionSource = interactionSource,
    ) {
        if (leadingIcon != null) {
            Icon(
                modifier = Modifier.size(SdsButtonDefaults.IconSize),
                imageVector = leadingIcon,
                contentDescription = null,
            )
            Spacer(Modifier.width(SdsButtonDefaults.IconSpacing))
        }
        Text(
            text = text,
            maxLines = 1,
        )
    }
}

@Composable
fun SdsButton(
    buttonVariant: ButtonVariant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SdsButtonDefaults.shape,
    buttonSize: ButtonSize = ButtonSize.Medium,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit
) {
    SdsButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = SdsButtonDefaults.buttonColors(buttonVariant),
        border = SdsButtonDefaults.buttonBorder(buttonVariant, enabled),
        style = SdsButtonDefaults.buttonTextStyle(buttonSize),
        contentPadding = SdsButtonDefaults.contentPadding(buttonSize),
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun SdsOutlineButton(
    buttonVariant: ButtonVariant,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SdsButtonDefaults.shape,
    buttonSize: ButtonSize = ButtonSize.Small,
    leadingIcon: ImageVector? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    SdsOutlineButton(
        buttonVariant = buttonVariant,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = SdsButtonDefaults.buttonBorder(buttonVariant, enabled),
        buttonSize = buttonSize,
        interactionSource = interactionSource,
    ) {
        if (leadingIcon != null) {
            Icon(
                modifier = Modifier.size(SdsButtonDefaults.IconSize),
                imageVector = leadingIcon,
                contentDescription = null,
            )
            Spacer(Modifier.width(SdsButtonDefaults.IconSpacing))
        }
        Text(
            text = text,
            maxLines = 1,
        )
    }
}

@Composable
fun SdsOutlineButton(
    buttonVariant: ButtonVariant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SdsButtonDefaults.shape,
    border: BorderStroke? = SdsButtonDefaults.buttonBorder(buttonVariant, enabled),
    buttonSize: ButtonSize = ButtonSize.Medium,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit
) {
    SdsButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = border,
        style = SdsButtonDefaults.buttonTextStyle(buttonSize),
        contentPadding = SdsButtonDefaults.contentPadding(buttonSize),
        interactionSource = interactionSource,
        colors = SdsButtonDefaults.outlinedButtonColors(buttonVariant),
        content = content
    )
}

@Composable
fun SdsLoadingButton(
    buttonVariant: ButtonVariant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    enabled: Boolean = true,
    shape: Shape = SdsButtonDefaults.shape,
    buttonSize: ButtonSize = ButtonSize.Medium,
    interactionSource: MutableInteractionSource? = null,
) {
    SdsButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = SdsButtonDefaults.buttonColors(buttonVariant),
        border = SdsButtonDefaults.buttonBorder(buttonVariant, enabled),
        style = SdsButtonDefaults.buttonTextStyle(buttonSize),
        contentPadding = SdsButtonDefaults.contentPadding(buttonSize),
        interactionSource = interactionSource,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(SdsButtonDefaults.IconSize),
                color = LocalContentColor.current,
                strokeWidth = 2.dp,
            )
            Spacer(Modifier.width(SdsButtonDefaults.IconSpacing))
        }
        Text(
            text = text,
            maxLines = 1,
        )
    }
}

@Composable
private fun SdsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = SdsButtonDefaults.buttonColors(ButtonVariant.Primary),
    border: BorderStroke? = SdsButtonDefaults.buttonBorder(ButtonVariant.Primary, enabled),
    contentPadding: PaddingValues = SdsButtonDefaults.contentPadding(ButtonSize.Medium),
    style: TextStyle = SdsButtonDefaults.buttonTextStyle(ButtonSize.Medium),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val containerColor = colors.containerColor(enabled)
    val contentColor = colors.contentColor(enabled)

    SdsSurface(
        modifier = modifier
            .clip(shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            ),
        color = containerColor,
        contentColor = contentColor,
        border = border,
        shape = shape,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides style,
        ) {
            Row(
                modifier = Modifier.padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

object SdsButtonDefaults {
    val shape: Shape
        @Composable get() = RoundedCornerShape(8.dp)

    val IconSize: Dp = 16.dp

    val IconSpacing: Dp = 8.dp

    @Composable
    fun buttonColors(
        buttonVariant: ButtonVariant,
    ): ButtonColors = when (buttonVariant) {
        ButtonVariant.Primary -> ButtonColors(
            containerColor = SdsTheme.colorScheme.backgroundBrandDefault,
            contentColor = SdsTheme.colorScheme.onBackgroundBrandDefault,
            disabledContainerColor = SdsTheme.colorScheme.backgroundDisabled,
            disabledContentColor = SdsTheme.colorScheme.onBackgroundDisabled,
        )

        ButtonVariant.Neutral -> ButtonColors(
            containerColor = SdsTheme.colorScheme.backgroundNeutralTertiary,
            contentColor = SdsTheme.colorScheme.textDefaultDefault,
            disabledContainerColor = SdsTheme.colorScheme.backgroundDisabled,
            disabledContentColor = SdsTheme.colorScheme.onBackgroundDisabled,
        )

        ButtonVariant.Subtle -> ButtonColors(
            containerColor = Color.Transparent,
            contentColor = SdsTheme.colorScheme.textNeutralDefault,
            disabledContainerColor = SdsTheme.colorScheme.backgroundDisabled,
            disabledContentColor = SdsTheme.colorScheme.onBackgroundDisabled,
        )

        ButtonVariant.Danger -> ButtonColors(
            containerColor = SdsTheme.colorScheme.backgroundDangerDefault,
            contentColor = SdsTheme.colorScheme.onBackgroundDangerDefault,
            disabledContainerColor = SdsTheme.colorScheme.backgroundDisabled,
            disabledContentColor = SdsTheme.colorScheme.onBackgroundDisabled,
        )
    }

    @Composable
    fun outlinedButtonColors(buttonVariant: ButtonVariant): ButtonColors = ButtonColors(
        containerColor = Color.Transparent,
        contentColor = when (buttonVariant) {
            ButtonVariant.Danger -> SdsTheme.colorScheme.textDangerDefault
            else -> SdsTheme.colorScheme.textDefaultDefault
        },
        disabledContainerColor = Color.Transparent,
        disabledContentColor = SdsTheme.colorScheme.textDisabled,
    )

    @Composable
    fun contentPadding(
        buttonSize: ButtonSize
    ): PaddingValues = PaddingValues(
        horizontal = when (buttonSize) {
            ButtonSize.Small -> 12.dp
            ButtonSize.Medium -> 12.dp
        },
        vertical = when (buttonSize) {
            ButtonSize.Small -> 8.dp
            ButtonSize.Medium -> 12.dp
        }
    )

    @Composable
    fun buttonBorder(
        buttonVariant: ButtonVariant,
        enabled: Boolean
    ): BorderStroke = BorderStroke(
        width = OutlineWidth,
        color = if (enabled) {
            when (buttonVariant) {
                ButtonVariant.Primary -> SdsTheme.colorScheme.borderDefaultDefault
                ButtonVariant.Neutral -> SdsTheme.colorScheme.borderNeutralSecondary
                ButtonVariant.Subtle -> Color.Transparent
                ButtonVariant.Danger -> SdsTheme.colorScheme.borderDangerSecondary
            }
        } else {
            when (buttonVariant) {
                ButtonVariant.Subtle -> SdsTheme.colorScheme.borderDisabled
                else -> Color.Transparent
            }
        }
    )

    @Composable
    fun buttonTextStyle(
        buttonSize: ButtonSize
    ): TextStyle = when (buttonSize) {
        ButtonSize.Small -> SdsTheme.typography.bodyMedium
        ButtonSize.Medium -> SdsTheme.typography.bodyBase
    }
}

@Stable
data class ButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
)

@Composable
private fun ButtonColors.containerColor(enabled: Boolean): Color =
    if (enabled) containerColor else disabledContainerColor

@Composable
private fun ButtonColors.contentColor(enabled: Boolean): Color =
    if (enabled) contentColor else disabledContentColor

private val OutlineWidth = 1.dp
