package trillion.wms.feature.zone.list

import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.model.Zone

data class ZoneListUiState(
    val zones: UiResult<List<Zone>> = UiResult.Loading,
    val isRefreshing: Boolean = false,
    val sideEffect: SideEffect? = null,
) {

    sealed interface SideEffect {
        data class ShowSnackbar(val message: String) : SideEffect
    }
}
