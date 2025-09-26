package trillion.wms.feature.fabricroll.detail

import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.utils.UiMessage

data class FabricRollDetailUiState(
    val zone: UiResult<Zone> = UiResult.Loading,
    val fabricRoll: UiResult<FabricRoll> = UiResult.Loading,
    val outboundHistories: UiResult<List<OutboundHistory>> = UiResult.Loading,
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val isRefreshing: Boolean = false,
    val message: UiMessage? = null,
)
