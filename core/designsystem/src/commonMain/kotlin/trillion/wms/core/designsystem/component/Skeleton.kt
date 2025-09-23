package trillion.wms.core.designsystem.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    color: Color = SdsTheme.colorScheme.backgroundBrandSecondary,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmer(color)
    )
}

@Composable
private fun Modifier.shimmer(
    seedColor: Color,
) = composed {
    val colors = listOf(
        seedColor.copy(alpha = 0.6f),
        seedColor.copy(alpha = 0.5f),
        seedColor.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_500, delayMillis = 500),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ShimmerAnimation"
    )
    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation, y = 0f)
        )
    )
}
