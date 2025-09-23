package trillion.wms.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.theme.SdsTheme
import trillion.wms.core.designsystem.theme.contentColorFor

@Composable
fun SdsCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors().copy(
        containerColor = SdsTheme.colorScheme.backgroundDefaultDefault,
        contentColor = contentColorFor(SdsTheme.colorScheme.backgroundDefaultDefault)
    ),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = BorderStroke(
        width = 1.dp,
        color = SdsTheme.colorScheme.borderDefaultDefault,
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        content = content
    )
}

@Composable
fun SdsCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors().copy(
        containerColor = SdsTheme.colorScheme.backgroundDefaultDefault,
        contentColor = contentColorFor(SdsTheme.colorScheme.backgroundDefaultDefault)
    ),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = BorderStroke(
        width = 1.dp,
        color = SdsTheme.colorScheme.borderDefaultDefault,
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        content = content
    )
}
