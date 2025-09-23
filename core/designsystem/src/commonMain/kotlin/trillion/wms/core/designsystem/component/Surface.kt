package trillion.wms.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun SdsSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = SdsTheme.colorScheme.backgroundDefaultDefault,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        content = content
    )
}
