package trillion.wms.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import trillion.wms.core.designsystem.theme.SdsTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.floor
import kotlin.math.max

@Composable
fun SdsCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SdsCheckboxColors = SdsCheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null
) {
    val toggleableModifier = if (onCheckedChange != null) {
        Modifier.triStateToggleable(
            state = ToggleableState(checked),
            onClick = { onCheckedChange(!checked) },
            enabled = enabled,
            role = Role.Checkbox,
            interactionSource = interactionSource,
            indication = null,
        )
    } else {
        Modifier
    }
    CheckboxImpl(
        modifier = modifier
            .then(toggleableModifier),
        state = ToggleableState(checked),
        enabled = enabled,
        colors = colors,
    )
}

object SdsCheckboxDefaults {

    @Composable
    fun colors() = SdsCheckboxColors(
        checkedCheckmarkColor = SdsTheme.colorScheme.onBackgroundBrandDefault,
        uncheckedCheckmarkColor = Color.Transparent,
        checkedBoxColor = SdsTheme.colorScheme.backgroundBrandDefault,
        uncheckedBoxColor = SdsTheme.colorScheme.backgroundDefaultDefault,
        disabledCheckmarkColor = SdsTheme.colorScheme.onBackgroundDisabled,
        disabledBoxColor = SdsTheme.colorScheme.backgroundDisabled,
        checkedBorderColor = Color.Transparent,
        uncheckedBorderColor = SdsTheme.colorScheme.borderBrandTertiary,
        disabledBorderColor = SdsTheme.colorScheme.borderDisabled
    )
}

@Composable
private fun CheckboxImpl(
    modifier: Modifier,
    enabled: Boolean,
    state: ToggleableState,
    colors: SdsCheckboxColors,
) {
    val transition = updateTransition(state)
    val checkDrawFraction =
        transition.animateFloat(
            transitionSpec = {
                when {
                    initialState == ToggleableState.Off -> tween(CheckAnimationDuration)
                    targetState == ToggleableState.Off -> snap(BoxOutDuration)
                    else -> spring()
                }
            }
        ) {
            when (it) {
                ToggleableState.On -> 1f
                ToggleableState.Off -> 0f
                ToggleableState.Indeterminate -> 1f
            }
        }

    val checkCenterGravitationShiftFraction =
        transition.animateFloat(
            transitionSpec = {
                when {
                    initialState == ToggleableState.Off -> snap()
                    targetState == ToggleableState.Off -> snap(BoxOutDuration)
                    else -> tween(durationMillis = CheckAnimationDuration)
                }
            }
        ) {
            when (it) {
                ToggleableState.On -> 0f
                ToggleableState.Off -> 0f
                ToggleableState.Indeterminate -> 1f
            }
        }
    val checkCache = remember { CheckDrawingCache() }
    val checkColor = colors.checkmarkColor(enabled, state)
    val boxColor = colors.boxColor(enabled, state)
    val borderColor = colors.borderColor(enabled, state)
    Canvas(modifier.wrapContentSize(Alignment.Center).requiredSize(CheckboxSize)) {
        val borderWidthPx = floor(BorderWidth.toPx())
        val checkmarkStrokeWidthPx = floor(CheckmarkStrokeWidth.toPx())
        drawBox(
            boxColor = boxColor.value,
            borderColor = borderColor.value,
            radius = RadiusSize.toPx(),
            strokeWidth = borderWidthPx
        )
        drawCheck(
            checkColor = checkColor.value,
            checkFraction = checkDrawFraction.value,
            crossCenterGravitation = checkCenterGravitationShiftFraction.value,
            strokeWidthPx = checkmarkStrokeWidthPx,
            drawingCache = checkCache
        )
    }
}

private fun DrawScope.drawBox(
    boxColor: Color,
    borderColor: Color,
    radius: Float,
    strokeWidth: Float
) {
    val halfStrokeWidth = strokeWidth / 2.0f
    val stroke = Stroke(strokeWidth)
    val checkboxSize = size.width
    if (boxColor == borderColor) {
        drawRoundRect(
            boxColor,
            size = Size(checkboxSize, checkboxSize),
            cornerRadius = CornerRadius(radius),
            style = Fill
        )
    } else {
        drawRoundRect(
            boxColor,
            topLeft = Offset(strokeWidth, strokeWidth),
            size = Size(checkboxSize - strokeWidth * 2, checkboxSize - strokeWidth * 2),
            cornerRadius = CornerRadius(max(0f, radius - strokeWidth)),
            style = Fill
        )
        drawRoundRect(
            borderColor,
            topLeft = Offset(halfStrokeWidth, halfStrokeWidth),
            size = Size(checkboxSize - strokeWidth, checkboxSize - strokeWidth),
            cornerRadius = CornerRadius(radius - halfStrokeWidth),
            style = stroke
        )
    }
}

private fun DrawScope.drawCheck(
    checkColor: Color,
    checkFraction: Float,
    crossCenterGravitation: Float,
    strokeWidthPx: Float,
    drawingCache: CheckDrawingCache
) {
    val stroke = Stroke(width = strokeWidthPx, cap = StrokeCap.Square)
    val width = size.width
    val checkCrossX = 0.4f
    val checkCrossY = 0.7f
    val leftX = 0.2f
    val leftY = 0.5f
    val rightX = 0.8f
    val rightY = 0.3f

    val gravitatedCrossX = lerp(checkCrossX, 0.5f, crossCenterGravitation)
    val gravitatedCrossY = lerp(checkCrossY, 0.5f, crossCenterGravitation)
    // gravitate only Y for end to achieve center line
    val gravitatedLeftY = lerp(leftY, 0.5f, crossCenterGravitation)
    val gravitatedRightY = lerp(rightY, 0.5f, crossCenterGravitation)

    with(drawingCache) {
        checkPath.reset()
        checkPath.moveTo(width * leftX, width * gravitatedLeftY)
        checkPath.lineTo(width * gravitatedCrossX, width * gravitatedCrossY)
        checkPath.lineTo(width * rightX, width * gravitatedRightY)
        // TODO: replace with proper declarative non-android alternative when ready (b/158188351)
        pathMeasure.setPath(checkPath, false)
        pathToDraw.reset()
        pathMeasure.getSegment(0f, pathMeasure.length * checkFraction, pathToDraw, true)
    }
    drawPath(drawingCache.pathToDraw, checkColor, style = stroke)
}

@Immutable
private class CheckDrawingCache(
    val checkPath: Path = Path(),
    val pathMeasure: PathMeasure = PathMeasure(),
    val pathToDraw: Path = Path()
)


@Immutable
data class SdsCheckboxColors(
    val checkedCheckmarkColor: Color,
    val uncheckedCheckmarkColor: Color,
    val checkedBoxColor: Color,
    val uncheckedBoxColor: Color,
    val disabledCheckmarkColor: Color,
    val disabledBoxColor: Color,
    val checkedBorderColor: Color,
    val uncheckedBorderColor: Color,
    val disabledBorderColor: Color,
) {

    @Composable
    fun checkmarkColor(enabled: Boolean, state: ToggleableState): State<Color> {
        val target = if (enabled) {
            when (state) {
                ToggleableState.On, ToggleableState.Indeterminate -> checkedCheckmarkColor
                ToggleableState.Off -> uncheckedCheckmarkColor
            }
        } else {
            when (state) {
                ToggleableState.On,
                ToggleableState.Indeterminate -> disabledCheckmarkColor

                ToggleableState.Off -> uncheckedCheckmarkColor
            }
        }
        val duration = if (state == ToggleableState.Off) BoxOutDuration else BoxInDuration
        return animateColorAsState(target, tween(duration))
    }

    @Composable
    fun boxColor(enabled: Boolean, state: ToggleableState): State<Color> {
        val target = if (enabled) {
            when (state) {
                ToggleableState.On,
                ToggleableState.Indeterminate -> checkedBoxColor

                ToggleableState.Off -> uncheckedBoxColor
            }
        } else {
            disabledBoxColor
        }
        return if (enabled) {
            val duration = if (state == ToggleableState.Off) BoxOutDuration else BoxInDuration
            return animateColorAsState(target, tween(duration))
        } else {
            rememberUpdatedState(target)
        }
    }

    @Composable
    fun borderColor(enabled: Boolean, state: ToggleableState): State<Color> {
        val target = if (enabled) {
            when (state) {
                ToggleableState.On,
                ToggleableState.Indeterminate -> checkedBorderColor

                ToggleableState.Off -> uncheckedBorderColor
            }
        } else {
            disabledBorderColor
        }
        return if (enabled) {
            val duration = if (state == ToggleableState.Off) BoxOutDuration else BoxInDuration
            return animateColorAsState(target, tween(duration))
        } else {
            rememberUpdatedState(target)
        }
    }
}

private const val BoxInDuration = 50
private const val BoxOutDuration = 100
private const val CheckAnimationDuration = 100

private val CheckboxSize: Dp = 16.dp
private val BorderWidth: Dp = 1.dp
private val CheckmarkStrokeWidth: Dp = 1.6.dp
private val RadiusSize: Dp = 4.dp

@Composable
@Preview
private fun SdsCheckboxPreview() {
    SdsTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var checked by remember { mutableStateOf(true) }
            SdsCheckbox(checked = checked, onCheckedChange = { checked = !checked })
            SdsCheckbox(checked = !checked, onCheckedChange = { checked = !checked })
            SdsCheckbox(checked = true, onCheckedChange = {}, enabled = false)
            SdsCheckbox(checked = false, onCheckedChange = {}, enabled = false)
        }

    }
}
