package trillion.wms.feature.inventory

import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.InventorySummary
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.utils.UiMessage


data class InventoryUiState(
    val search: SearchUiState = SearchUiState(),
    val message: UiMessage? = null,
)

data class SearchUiState(
    val searchQuery: String = "",
    val filters: Filters = Filters(),
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val searchResults: UiResult<List<FabricRoll>> = UiResult.Loading,
) {

    data class Filters(
        val selectedZoneFilter: ZoneFilter = ZoneFilter.All,
        val availableZoneFilters: List<ZoneFilter> = emptyList(),
    )

    sealed interface ZoneFilter {
        data object All : ZoneFilter
        data class Selected(val zone: Zone) : ZoneFilter
    }
}

val SearchUiState.ZoneFilter.zoneId: Long?
    get() = (this as? SearchUiState.ZoneFilter.Selected)?.zone?.id
