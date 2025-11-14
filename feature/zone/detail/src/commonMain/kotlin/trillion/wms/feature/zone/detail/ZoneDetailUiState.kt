package trillion.wms.feature.zone.detail

import androidx.compose.runtime.Stable
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.utils.UiMessage

@Stable
data class ZoneDetailUiState(
    val zone: UiResult<Zone> = UiResult.Loading,
    val fabricRolls: UiResult<List<FabricRoll>> = UiResult.Loading,
    val searchQuery: String = "",
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val isRefreshing: Boolean = false,
    val message: UiMessage? = null,
)
