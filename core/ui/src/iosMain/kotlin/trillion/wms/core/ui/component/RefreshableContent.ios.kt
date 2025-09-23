package trillion.wms.core.ui.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import trillion.wms.core.designsystem.component.SdsPullToRefreshBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun RefreshableContent(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    SdsPullToRefreshBox(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        content = content,
    )
}
