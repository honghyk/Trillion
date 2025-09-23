package trillion.wms.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastFirstOrNull
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun SdsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = SdsTheme.typography.bodyMedium,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = SdsTextFieldDefaults.shape,
    colors: SdsTextFieldColors = SdsTextFieldDefaults.colors(),
    contentPadding: PaddingValues = SdsTextFieldDefaults.contentPadding,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled)
    }
    val mergedTextStyle = textStyle.merge(color = textColor)
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(colors.cursorColor),
        decorationBox = @Composable { innerTextField ->
            SdsTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = enabled,
                isError = isError,
                label = label,
                placeholder = placeholder,
                trailingIcon = trailingIcon,
                supportingText = supportingText,
                shape = shape,
                colors = colors,
                contentPadding = contentPadding,
            )
        }
    )
}

@Composable
private fun TextFieldLayout(
    modifier: Modifier,
    textField: @Composable () -> Unit,
    label: @Composable (() -> Unit)?,
    placeholder: @Composable (() -> Unit)?,
    trailing: @Composable (() -> Unit)?,
    supportingText: @Composable (() -> Unit)?,
    container: @Composable () -> Unit,
    paddingValues: PaddingValues,
) {
    val layoutDirection = LocalLayoutDirection.current
    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier.layoutId(ContainerId),
                propagateMinConstraints = true,
            ) {
                container()
            }
            if (label != null) {
                Box(
                    modifier = Modifier.layoutId(LabelId)
                        .padding(bottom = 8.dp)
                ) {
                    label()
                }
            }
            val endPadding = paddingValues.calculateEndPadding(layoutDirection)
            if (trailing != null) {
                Box(modifier = Modifier.layoutId(TrailingId).padding(end = endPadding)) {
                    trailing()
                }
            }
            if (placeholder != null) {
                Box(modifier = Modifier.layoutId(PlaceholderId).padding(paddingValues)) {
                    placeholder()
                }
            }
            if (supportingText != null) {
                Box(
                    modifier = Modifier.layoutId(SupportingId)
                        .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                ) {
                    supportingText()
                }
            }

            Box(
                modifier = Modifier.layoutId(TextFieldId).padding(paddingValues),
                propagateMinConstraints = true,
            ) {
                textField()
            }
        }
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val labelPlaceable =
            measurables.fastFirstOrNull { it.layoutId == LabelId }?.measure(looseConstraints)
        val trailingPlaceable =
            measurables.fastFirstOrNull { it.layoutId == TrailingId }?.measure(looseConstraints)

        val supportingMeasurable = measurables.fastFirstOrNull { it.layoutId == SupportingId }

        val textFieldConstraints = constraints
            .offset(
                horizontal = -widthOrZero(trailingPlaceable),
                vertical = -heightOrZero(labelPlaceable),
            )
        val textFieldPlaceable =
            measurables.fastFirst { it.layoutId == TextFieldId }.measure(textFieldConstraints)
        val placeholderPlaceable =
            measurables
                .fastFirstOrNull { it.layoutId == PlaceholderId }
                ?.measure(textFieldConstraints.copy(minWidth = 0))

        val totalWeight = calculateWidth(
            textFieldWidth = textFieldPlaceable.width,
            placeholderWidth = widthOrZero(placeholderPlaceable),
            trailingWidth = widthOrZero(trailingPlaceable),
            constraints = constraints,
        )

        val supportingConstraints = looseConstraints
            .offset(vertical = -heightOrZero(textFieldPlaceable))
            .copy(minHeight = 0, maxWidth = totalWeight)
        val supportingPlaceable = supportingMeasurable?.measure(supportingConstraints)

        val totalHeight = calculateHeight(
            textFieldHeight = textFieldPlaceable.height,
            labelHeight = heightOrZero(labelPlaceable),
            supportingHeight = heightOrZero(supportingPlaceable),
            constraints = constraints,
        )

        val containerPlaceable =
            measurables
                .fastFirst { it.layoutId == ContainerId }
                .measure(
                    Constraints(
                        minWidth = if (totalWeight != Constraints.Infinity) totalWeight else 0,
                        maxWidth = totalWeight,
                        minHeight = if (textFieldPlaceable.height != Constraints.Infinity) textFieldPlaceable.height else 0,
                        maxHeight = textFieldPlaceable.height
                    )
                )

        layout(totalWeight, totalHeight) {
            labelPlaceable?.placeRelative(IntOffset.Zero)

            val textFieldYPosition = heightOrZero(labelPlaceable)
            containerPlaceable.placeRelative(0, textFieldYPosition)
            textFieldPlaceable.placeRelative(0, textFieldYPosition)
            placeholderPlaceable?.placeRelative(0, textFieldYPosition)
            supportingPlaceable?.placeRelative(0, textFieldYPosition + containerPlaceable.height)

            trailingPlaceable?.placeRelative(
                textFieldPlaceable.width,
                textFieldYPosition + (textFieldPlaceable.height - heightOrZero(trailingPlaceable)) / 2
            )
        }
    }
}

private fun calculateHeight(
    textFieldHeight: Int,
    labelHeight: Int,
    supportingHeight: Int,
    constraints: Constraints,
): Int {
    return maxOf(
        constraints.minHeight,
        textFieldHeight + labelHeight + supportingHeight,
    )
}

private fun calculateWidth(
    textFieldWidth: Int,
    placeholderWidth: Int,
    trailingWidth: Int,
    constraints: Constraints,
): Int {
    val textWidth = maxOf(textFieldWidth, placeholderWidth)
    return maxOf(
        constraints.minWidth,
        textWidth + trailingWidth,
    )
}

private fun heightOrZero(placeable: Placeable?): Int = placeable?.height ?: 0
private fun widthOrZero(placeable: Placeable?): Int = placeable?.width ?: 0

private const val TextFieldId = "TextField"
private const val LabelId = "Label"
private const val PlaceholderId = "Placeholder"
private const val TrailingId = "TrailingIcon"
private const val SupportingId = "SupportingText"
private const val ContainerId = "Container"

object SdsTextFieldDefaults {
    val shape: Shape
        @Composable get() = RoundedCornerShape(8.dp)

    val contentPadding: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

    val borderThickness: Dp = 1.dp

    @Composable
    fun colors() = SdsTextFieldColors(
        textColor = SdsTheme.colorScheme.textDefaultDefault,
        disabledTextColor = SdsTheme.colorScheme.onBackgroundDefaultSecondary,
        containerColor = SdsTheme.colorScheme.backgroundDefaultTertiary,
        disabledContainerColor = SdsTheme.colorScheme.backgroundDefaultSecondary,
        placeholderColor = SdsTheme.colorScheme.textDefaultTertiary,
        disabledPlaceholderColor = SdsTheme.colorScheme.textDisabled,
        labelColor = SdsTheme.colorScheme.textDefaultDefault,
        disabledLabelColor = SdsTheme.colorScheme.textDisabled,
        trailingIconColor = SdsTheme.colorScheme.iconDefaultDefault,
        disabledTrailingIconColor = SdsTheme.colorScheme.iconDisabled,
        supportingTextColor = SdsTheme.colorScheme.textDefaultDefault,
        disabledSupportingTextColor = SdsTheme.colorScheme.textDisabled,
        errorSupportingTextColor = SdsTheme.colorScheme.textDangerDefault,
        borderColor = SdsTheme.colorScheme.borderDefaultDefault,
        disabledBorderColor = SdsTheme.colorScheme.borderDisabled,
        errorBorderColor = SdsTheme.colorScheme.borderDangerDefault,
        cursorColor = SdsTheme.colorScheme.textDefaultDefault,
    )

    @Composable
    fun DecorationBox(
        value: String,
        innerTextField: @Composable () -> Unit,
        enabled: Boolean,
        isError: Boolean,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        shape: Shape = SdsTextFieldDefaults.shape,
        colors: SdsTextFieldColors = colors(),
        contentPadding: PaddingValues = SdsTextFieldDefaults.contentPadding,
        container: @Composable () -> Unit = {
            Container(
                modifier = Modifier,
                enabled = enabled,
                isError = isError,
                shape = shape,
                colors = colors,
                borderThickness = borderThickness,
            )
        },
    ) {
        val decoratedLabel = label?.let { label ->
            @Composable {
                Decoration(
                    contentColor = colors.labelColor(enabled),
                    textStyle = SdsTheme.typography.bodyMedium,
                    content = label
                )
            }
        }
        val decoratedPlaceholder = if (placeholder != null && value.isEmpty()) {
            @Composable {
                Decoration(
                    contentColor = colors.placeholderColor(enabled),
                    textStyle = SdsTheme.typography.bodyMedium,
                    content = placeholder
                )
            }
        } else null
        val decoratedTrailingIcon = trailingIcon?.let { trailingIcon ->
            @Composable {
                Decoration(
                    contentColor = colors.trailingIconColor(enabled),
                    content = trailingIcon
                )
            }
        }

        val decoratedSupportingText = if (supportingText != null) {
            @Composable {
                Decoration(
                    contentColor = colors.supportingTextColor(enabled, isError),
                    textStyle = SdsTheme.typography.bodySmall,
                    content = supportingText
                )
            }
        } else null

        TextFieldLayout(
            modifier = Modifier,
            textField = innerTextField,
            label = decoratedLabel,
            placeholder = decoratedPlaceholder,
            trailing = decoratedTrailingIcon,
            supportingText = decoratedSupportingText,
            paddingValues = contentPadding,
            container = container,
        )
    }

    @Composable
    fun Container(
        modifier: Modifier,
        enabled: Boolean,
        isError: Boolean,
        shape: Shape,
        colors: SdsTextFieldColors,
        borderThickness: Dp,
    ) {
        val containerColor = colors.containerColor(enabled)
        val borderColor = colors.borderColor(enabled, isError)
        Box(
            modifier = modifier
                .background(color = containerColor, shape = shape)
                .border(
                    width = borderThickness,
                    color = borderColor,
                    shape = shape,
                )
        )
    }
}

@Immutable
data class SdsTextFieldColors(
    val textColor: Color,
    val disabledTextColor: Color,
    val containerColor: Color,
    val disabledContainerColor: Color,
    val errorBorderColor: Color,
    val placeholderColor: Color,
    val disabledPlaceholderColor: Color,
    val labelColor: Color,
    val disabledLabelColor: Color,
    val trailingIconColor: Color,
    val disabledTrailingIconColor: Color,
    val borderColor: Color,
    val disabledBorderColor: Color,
    val supportingTextColor: Color,
    val disabledSupportingTextColor: Color,
    val errorSupportingTextColor: Color,
    val cursorColor: Color,
) {

    @Stable
    fun textColor(enabled: Boolean): Color =
        if (enabled) textColor else disabledTextColor

    @Stable
    fun containerColor(enabled: Boolean): Color =
        if (enabled) containerColor else disabledContainerColor

    @Stable
    fun placeholderColor(enabled: Boolean): Color =
        if (enabled) placeholderColor else disabledPlaceholderColor

    @Stable
    fun labelColor(enabled: Boolean): Color =
        if (enabled) labelColor else disabledLabelColor

    @Stable
    fun trailingIconColor(enabled: Boolean): Color =
        if (enabled) trailingIconColor else disabledTrailingIconColor

    @Stable
    fun borderColor(enabled: Boolean, isError: Boolean): Color =
        when {
            !enabled -> disabledBorderColor
            isError -> errorBorderColor
            else -> borderColor
        }

    @Stable
    fun supportingTextColor(enabled: Boolean, isError: Boolean): Color =
        when {
            !enabled -> disabledSupportingTextColor
            isError -> errorSupportingTextColor
            else -> supportingTextColor
        }
}

@Composable
private fun Decoration(contentColor: Color, textStyle: TextStyle, content: @Composable () -> Unit) =
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides textStyle,
        content = content
    )

@Composable
private fun Decoration(contentColor: Color, content: @Composable () -> Unit) =
    CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
