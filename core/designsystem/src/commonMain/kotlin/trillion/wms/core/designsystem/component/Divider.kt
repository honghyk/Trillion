package trillion.wms.core.designsystem.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun SdsDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = SdsTheme.colorScheme.borderDefaultDefault,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}
