package trillion.wms.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun DashboardGrid(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    content: @Composable FlowRowScope.() -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val maxItemsInEachRow = when {
        windowSizeClass.minWidthDp.dp < WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND.dp -> 2
        windowSizeClass.minWidthDp.dp < WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND.dp -> 3
        else -> 4
    }

    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = maxItemsInEachRow,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
    ) {
        content()
    }
}

@Composable
fun DashboardCard(
    icon: ImageVector,
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    val windowMinWidth = currentWindowAdaptiveInfo().windowSizeClass.minWidthDp.dp
    if (windowMinWidth < WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND.dp) {
        SimpleDashboardCard(
            icon = icon,
            title = title,
            content = content,
            modifier = modifier,
        )
    } else {
        FullDashboardCard(
            icon = icon,
            title = title,
            content = content,
            modifier = modifier,
            description = description,
        )
    }
}

@Composable
private fun FullDashboardCard(
    icon: ImageVector,
    title: String,
    content: String,
    modifier: Modifier,
    description: String? = null,
) {
    SdsCard(modifier = modifier) {
        Box(modifier = Modifier.padding(24.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = title,
                        style = SdsTheme.typography.bodyMedium,
                        maxLines = 1,
                    )
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Spacer(Modifier.height(40.dp))
                Text(
                    text = content,
                    style = SdsTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                if (description != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = SdsTheme.typography.bodySmall,
                        color = SdsTheme.colorScheme.textDefaultSecondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleDashboardCard(
    icon: ImageVector,
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    SdsCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = SdsTheme.colorScheme.backgroundDefaultTertiary,
            contentColor = SdsTheme.colorScheme.textDefaultDefault,
        )
    ) {
        Box(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = SdsTheme.colorScheme.backgroundDefaultSecondary,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = content,
                        style = SdsTheme.typography.bodyStrong,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = title,
                        style = SdsTheme.typography.bodySmall,
                        color = SdsTheme.colorScheme.textDefaultSecondary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
