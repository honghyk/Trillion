package trillion.wms.feature.inventory

import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.InventorySummary
import trillion.wms.core.model.Zone


data class InventoryUiState(
    val inventorySummary: InventorySummaryUiState = InventorySummaryUiState(),
    val search: SearchUiState = SearchUiState(),
    val isRefreshing: Boolean = false,
    val sideEffect: SideEffect? = null,
) {

    sealed interface SideEffect {
        data class ShowSnackbar(val message: String) : SideEffect
    }
}

data class InventorySummaryUiState(
    val lengthUnit: LengthUnit = LengthUnit.METER,
    val inventorySummary: UiResult<InventorySummary> = UiResult.Loading,
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
