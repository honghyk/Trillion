package trillion.wms.core.designsystem.extensions

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp

internal fun Modifier.drawBorder(
    direction: Direction,
    width: Dp,
    color: Color,
) = drawWithContent {
    drawContent()
    drawLine(
        color = color,
        start = when (direction) {
            Direction.LEFT, Direction.TOP -> Offset(0f, 0f)
            Direction.RIGHT -> Offset(size.width, 0f)
            Direction.BOTTOM -> Offset(0f, size.height)
        },
        end = when (direction) {
            Direction.LEFT -> Offset(0f, size.height)
            Direction.RIGHT -> Offset(size.width, size.height)
            Direction.TOP -> Offset(size.width, 0f)
            Direction.BOTTOM -> Offset(size.width, size.height)
        },
        strokeWidth = width.toPx()
    )
}

internal enum class Direction {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
}

fun Modifier.hideKeyboardOnClick() = composed {
    val keyboardController = LocalSoftwareKeyboardController.current
    clickable(
        interactionSource = null,
        indication = null,
        onClick = { keyboardController?.hide() }
    )
}
