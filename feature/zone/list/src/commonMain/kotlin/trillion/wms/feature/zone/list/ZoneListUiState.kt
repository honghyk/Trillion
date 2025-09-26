package trillion.wms.feature.zone.list

import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.utils.UiMessage

data class ZoneListUiState(
    val zones: UiResult<List<Zone>> = UiResult.Loading,
    val isRefreshing: Boolean = false,
    val message: UiMessage? = null,
)
