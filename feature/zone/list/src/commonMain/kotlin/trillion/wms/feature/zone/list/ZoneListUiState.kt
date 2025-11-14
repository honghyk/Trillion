package trillion.wms.feature.zone.list

import androidx.compose.runtime.Stable
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.utils.UiMessage
import trillion.wms.core.ui.utils.UiResult

@Stable
data class ZoneListUiState(
    val zones: UiResult<List<Zone>> = UiResult.Loading,
    val isRefreshing: Boolean = false,
    val message: UiMessage? = null,
)
