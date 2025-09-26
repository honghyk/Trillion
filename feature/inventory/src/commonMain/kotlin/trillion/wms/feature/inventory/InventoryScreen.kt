package trillion.wms.feature.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import trillion.wms.core.designsystem.component.ButtonVariant
import trillion.wms.core.designsystem.component.DashboardCard
import trillion.wms.core.designsystem.component.DashboardGrid
import trillion.wms.core.designsystem.component.DropdownTextField
import trillion.wms.core.designsystem.component.Icons
import trillion.wms.core.designsystem.component.SdsOutlineButton
import trillion.wms.core.designsystem.component.SdsScaffold
import trillion.wms.core.designsystem.component.SdsTextField
import trillion.wms.core.designsystem.component.SdsTopAppBar
import trillion.wms.core.designsystem.extensions.hideKeyboardOnClick
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.InventorySummary
import trillion.wms.core.ui.component.ErrorContent
import trillion.wms.core.ui.component.FabricRollTable
import trillion.wms.core.ui.component.LengthUnitToggleButtons
import trillion.wms.core.ui.component.LoadingContent
import trillion.wms.core.ui.component.RefreshableContent
import trillion.wms.core.ui.compositionlocal.safeDrawingWithBottomNavBar
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.ui.utils.UiSideEffectHandler
import trillion.wms.core.ui.utils.formatDecimal
import trillion.wms.feature.inventory.SearchUiState.Filters
import trillion.wms.feature.inventory.SearchUiState.ZoneFilter

@Composable
fun InventoryScreen(
    onFabricRollTableItemClick: (Long) -> Unit,
    onOutboundFabricRollClick: (Long) -> Unit,
    onEditFabricRollClick: (Long, Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    InventoryScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onQueryChange = viewModel::updateSearchQuery,
        onZoneFilterSelect = viewModel::updateZoneFilter,
        onLengthUnitSelect = viewModel::updateDisplayLengthUnit,
        onFabricRollTableItemClick = { roll -> onFabricRollTableItemClick(roll.id) },
        onOutboundFabricRollClick = { roll -> onOutboundFabricRollClick(roll.id) },
        onEditFabricRollClick = { roll -> onEditFabricRollClick(roll.zoneId, roll.id) },
        onDeleteFabricRollClick = viewModel::deleteFabricRoll,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )

    UiSideEffectHandler(
        effect = uiState.sideEffect,
        onConsumed = viewModel::onSideEffectConsumed,
    ) { effect ->
        when (effect) {
            is InventoryUiState.SideEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(effect.message)
            }
        }
    }
}

@Composable
private fun InventoryScreen(
    uiState: InventoryUiState,
    snackbarHostState: SnackbarHostState,
    onQueryChange: (String) -> Unit,
    onZoneFilterSelect: (ZoneFilter) -> Unit,
    onLengthUnitSelect: (LengthUnit) -> Unit,
    onFabricRollTableItemClick: (FabricRoll) -> Unit,
    onOutboundFabricRollClick: (FabricRoll) -> Unit,
    onEditFabricRollClick: (FabricRoll) -> Unit,
    onDeleteFabricRollClick: (FabricRoll) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsScaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawingWithBottomNavBar
            .exclude(WindowInsets.ime),
    ) { contentPadding ->
        RefreshableContent(
            modifier = Modifier.padding(contentPadding),
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
        ) {
            InventoryContent(
                modifier = Modifier
                    .fillMaxSize()
                    .hideKeyboardOnClick(),
                inventorySummaryUiState = uiState.inventorySummary,
                searchUiState = uiState.search,
                onQueryChange = onQueryChange,
                onZoneFilterSelect = onZoneFilterSelect,
                onLengthUnitSelect = onLengthUnitSelect,
                onFabricRollTableItemClick = onFabricRollTableItemClick,
                onOutboundFabricRollClick = onOutboundFabricRollClick,
                onEditFabricRollClick = onEditFabricRollClick,
                onDeleteFabricRollClick = onDeleteFabricRollClick,
                onRefresh = onRefresh,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsTopAppBar(
        modifier = modifier,
        title = { Text("재고 관리") },
        actions = {
            RefreshIcon(
                isRefreshing = isRefreshing,
                onClick = onRefresh,
            )
        }
    )
}

@Composable
private fun InventoryContent(
    inventorySummaryUiState: InventorySummaryUiState,
    searchUiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onZoneFilterSelect: (ZoneFilter) -> Unit,
    onLengthUnitSelect: (LengthUnit) -> Unit,
    onFabricRollTableItemClick: (FabricRoll) -> Unit,
    onDeleteFabricRollClick: (FabricRoll) -> Unit,
    onEditFabricRollClick: (FabricRoll) -> Unit,
    onOutboundFabricRollClick: (FabricRoll) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InventorySummaryContent(
                inventorySummaryUiState = inventorySummaryUiState,
            )
            FilterableSearchInput(
                query = searchUiState.searchQuery,
                filters = searchUiState.filters,
                onQueryChange = onQueryChange,
                onZoneFilterSelected = onZoneFilterSelect,
                modifier = Modifier.fillMaxWidth(),
            )
            FabricRollsTableHeader(
                lengthUnit = searchUiState.lengthUnit,
                onLengthUnitSelected = onLengthUnitSelect,
            )
            when (searchUiState.searchResults) {
                is UiResult.Loading -> LoadingContent(
                    modifier = Modifier.weight(1f),
                )

                is UiResult.Error -> ErrorContent(
                    onRetryClick = onRefresh,
                    modifier = Modifier.weight(1f),
                )

                is UiResult.Success -> FabricRollsTable(
                    fabricRolls = searchUiState.searchResults.data,
                    lengthUnit = searchUiState.lengthUnit,
                    onFabricRollTableItemClick = onFabricRollTableItemClick,
                    onOutboundFabricRollClick = onOutboundFabricRollClick,
                    onEditFabricRollClick = onEditFabricRollClick,
                    onDeleteFabricRollClick = onDeleteFabricRollClick,
                    modifier = Modifier.heightIn(max = this@BoxWithConstraints.maxHeight - 32.dp),
                )
            }
        }
    }
}

@Composable
private fun InventorySummaryContent(
    inventorySummaryUiState: InventorySummaryUiState,
    modifier: Modifier = Modifier,
) {
    val inventorySummary = (inventorySummaryUiState.inventorySummary as? UiResult.Success)?.data
        ?: InventorySummary.EMPTY

    InventorySummaryDashboard(
        inventorySummary = inventorySummary,
        lengthUnit = inventorySummaryUiState.lengthUnit,
        modifier = modifier
    )
}

@Composable
private fun InventorySummaryDashboard(
    inventorySummary: InventorySummary,
    lengthUnit: LengthUnit,
    modifier: Modifier = Modifier,
) {
    DashboardGrid(modifier = modifier) {
        DashboardCard(
            icon = vectorResource(Icons.Package),
            title = "총 롤 수",
            content = inventorySummary.totalRollCount.formatDecimal(),
            description = "재고 내 원단 롤",
            modifier = Modifier.weight(1f),
        )
        DashboardCard(
            icon = vectorResource(Icons.Package),
            title = "총 수량",
            content = (inventorySummary.totalQuantity * lengthUnit.multiplier).formatDecimal(1) + " ${lengthUnit.abbreviateName}",
            description = "재고 내 원단 수량",
            modifier = Modifier.weight(1f),
        )
        DashboardCard(
            icon = vectorResource(Icons.Package),
            title = "총 구역",
            content = inventorySummary.totalZoneCount.formatDecimal(),
            description = "저장 구역",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun FilterableSearchInput(
    query: String,
    filters: Filters,
    onQueryChange: (String) -> Unit,
    onZoneFilterSelected: (ZoneFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    AdaptiveSearchLayout(
        textFieldMaxWidth = 400.dp,
        verticalSpacing = 8.dp,
        horizontalSpacing = 16.dp,
        textField = {
            SdsTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = "번호, 품목번호, 주문번호, 색상, 공장, 마감으로 검색...",
                        maxLines = 1,
                    )
                },
                singleLine = true,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            )
        },
        filterDropdown = {
            ZoneFilterDropdown(
                selected = filters.selectedZoneFilter,
                options = filters.availableZoneFilters,
                onOptionSelected = onZoneFilterSelected,
                modifier = Modifier.widthIn(max = 200.dp),
            )
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneFilterDropdown(
    selected: ZoneFilter,
    options: List<ZoneFilter>,
    onOptionSelected: (ZoneFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownTextField(
        selected = when (selected) {
            ZoneFilter.All -> "모든 구역"
            is ZoneFilter.Selected -> selected.zone.name
        },
        onSelected = { index -> onOptionSelected(options[index]) },
        options = options.map { zoneFilter ->
            when (zoneFilter) {
                ZoneFilter.All -> "모든 구역"
                is ZoneFilter.Selected -> zoneFilter.zone.name
            }
        },
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun FabricRollsTableHeader(
    lengthUnit: LengthUnit,
    onLengthUnitSelected: (LengthUnit) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "표시 단위:")
        LengthUnitToggleButtons(
            selected = lengthUnit,
            onSelected = onLengthUnitSelected
        )
    }
}

@Composable
private fun FabricRollsTable(
    fabricRolls: List<FabricRoll>,
    lengthUnit: LengthUnit,
    onFabricRollTableItemClick: (FabricRoll) -> Unit,
    onOutboundFabricRollClick: (FabricRoll) -> Unit,
    onEditFabricRollClick: (FabricRoll) -> Unit,
    onDeleteFabricRollClick: (FabricRoll) -> Unit,
    modifier: Modifier = Modifier,
) {
    FabricRollTable(
        modifier = modifier.fillMaxWidth(),
        fabricRolls = fabricRolls,
        lengthUnit = lengthUnit,
        onRowClick = { fabricRoll -> onFabricRollTableItemClick(fabricRoll) },
        actionsCell = { fabricRoll ->
            SdsOutlineButton(
                buttonVariant = ButtonVariant.Primary,
                leadingIcon = vectorResource(Icons.Minus),
                text = "출고",
                onClick = { onOutboundFabricRollClick(fabricRoll) }
            )
            SdsOutlineButton(
                buttonVariant = ButtonVariant.Primary,
                leadingIcon = vectorResource(Icons.Edit),
                text = "수정",
                onClick = { onEditFabricRollClick(fabricRoll) }
            )
            SdsOutlineButton(
                buttonVariant = ButtonVariant.Primary,
                leadingIcon = vectorResource(Icons.Trash),
                text = "삭제",
                onClick = { onDeleteFabricRollClick(fabricRoll) }
            )
        }
    )
}

@Composable
private fun AdaptiveSearchLayout(
    textFieldMaxWidth: Dp,
    horizontalSpacing: Dp,
    verticalSpacing: Dp,
    modifier: Modifier = Modifier,
    textField: @Composable () -> Unit,
    filterDropdown: @Composable () -> Unit,
) {
    SubcomposeLayout(
        modifier = modifier,
    ) { constraints ->
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val verticalSpacingPx = verticalSpacing.roundToPx()
        val textFieldMaxWidthPx = textFieldMaxWidth.roundToPx()

        val filterLookaheadPlaceable = subcompose(0, filterDropdown)
            .first()
            .measure(constraints.copy(minWidth = 0))

        val availableTextFieldWidth =
            constraints.maxWidth - filterLookaheadPlaceable.width - horizontalSpacingPx

        if (availableTextFieldWidth > textFieldMaxWidthPx) {
            val textFieldPlaceable = subcompose(1, textField)
                .first()
                .measure(Constraints.fixedWidth(availableTextFieldWidth))

            val width = constraints.maxWidth
            val height = maxOf(textFieldPlaceable.height, filterLookaheadPlaceable.height)

            layout(width, height) {
                textFieldPlaceable.placeRelative(0, 0)
                filterLookaheadPlaceable.placeRelative(
                    x = textFieldPlaceable.width + horizontalSpacingPx,
                    y = (height - filterLookaheadPlaceable.height) / 2,
                )
            }
        } else {
            val textFieldPlaceable = subcompose(1, textField)
                .first()
                .measure(constraints.copy(minWidth = constraints.maxWidth))
            val filterPlaceable = subcompose(2, filterDropdown)
                .first()
                .measure(constraints.copy(minWidth = constraints.maxWidth))

            val width = constraints.maxWidth
            val height = textFieldPlaceable.height + filterPlaceable.height + verticalSpacingPx

            layout(width, height) {
                textFieldPlaceable.placeRelative(0, 0)
                filterPlaceable.placeRelative(
                    x = 0,
                    y = textFieldPlaceable.height + verticalSpacingPx
                )
            }
        }
    }
}
