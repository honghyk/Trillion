package trillion.wms.feature.zone.detail

import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.Zone

data class ZoneDetailUiState(
    val zone: UiResult<Zone> = UiResult.Loading,
    val fabricRolls: UiResult<List<FabricRoll>> = UiResult.Loading,
    val searchQuery: String = "",
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val isRefreshing: Boolean = false,
    val sideEffect: SideEffect? = null,
) {
    sealed interface SideEffect {
        data class ShowSnackbar(val message: String) : SideEffect
    }
}
