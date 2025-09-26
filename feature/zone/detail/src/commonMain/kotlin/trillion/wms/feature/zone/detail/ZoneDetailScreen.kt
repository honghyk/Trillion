package trillion.wms.feature.zone.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import trillion.wms.core.ui.component.ErrorContent
import trillion.wms.core.ui.component.FabricRollTable
import trillion.wms.core.ui.component.LengthUnitToggleButtons
import trillion.wms.core.ui.component.LoadingContent
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.InstantFormatter
import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.ui.utils.formatDecimal
import trillion.wms.core.designsystem.component.ButtonSize
import trillion.wms.core.designsystem.component.ButtonVariant
import trillion.wms.core.designsystem.component.DashboardCard
import trillion.wms.core.designsystem.component.DashboardGrid
import trillion.wms.core.designsystem.component.Icons
import trillion.wms.core.designsystem.component.SdsButton
import trillion.wms.core.designsystem.component.SdsOutlineButton
import trillion.wms.core.designsystem.component.SdsScaffold
import trillion.wms.core.designsystem.component.SdsTextField
import trillion.wms.core.designsystem.component.SdsTopAppBar
import trillion.wms.core.designsystem.extensions.hideKeyboardOnClick
import trillion.wms.core.designsystem.theme.SdsTheme
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.component.RefreshableContent
import trillion.wms.core.ui.compositionlocal.safeDrawingWithBottomNavBar
import trillion.wms.core.ui.utils.UiSideEffectHandler

@Composable
fun ZoneDetailScreen(
    zoneId: Long,
    onBackClick: () -> Unit,
    onAddFabricRollClick: (zoneId: Long) -> Unit,
    onEditFabricRollClick: (zoneId: Long, rollId: Long) -> Unit,
    onOutboundFabricRollClick: (rollId: Long) -> Unit,
    onFabricRollTableItemClick: (rollId: Long) -> Unit,
    viewModel: ZoneDetailViewModel = koinViewModel { parametersOf(zoneId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    ZoneDetailScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onRefresh = viewModel::refresh,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onLengthUnitSelect = viewModel::updateLengthUnit,
        onFabricRollTableItemClick = { roll -> onFabricRollTableItemClick(roll.id) },
        onAddFabricRollClick = onAddFabricRollClick,
        onEditFabricRollClick = { roll -> onEditFabricRollClick(roll.zoneId, roll.id) },
        onOutboundFabricRollClick = { roll -> onOutboundFabricRollClick(roll.id) },
        onDeleteFabricRollClick = viewModel::deleteFabricRoll,
    )

    UiSideEffectHandler(
        effect = uiState.sideEffect,
        onConsumed = viewModel::onSideEffectConsumed,
    ) {
        when (it) {
            is ZoneDetailUiState.SideEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneDetailScreen(
    uiState: ZoneDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onLengthUnitSelect: (LengthUnit) -> Unit,
    onFabricRollTableItemClick: (FabricRoll) -> Unit,
    onAddFabricRollClick: (zoneId: Long) -> Unit,
    onEditFabricRollClick: (FabricRoll) -> Unit,
    onOutboundFabricRollClick: (FabricRoll) -> Unit,
    onDeleteFabricRollClick: (FabricRoll) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsScaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                isRefreshing = uiState.isRefreshing,
                onBackClick = onBackClick,
                onRefresh = onRefresh,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawingWithBottomNavBar
            .exclude(WindowInsets.ime),
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            when (uiState.zone) {
                is UiResult.Loading -> LoadingContent()
                is UiResult.Error -> ErrorContent(onRetryClick = onRefresh)

                is UiResult.Success -> {
                    RefreshableContent(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = onRefresh,
                    ) {
                        ZoneDetailContent(
                            zone = uiState.zone.data,
                            searchQuery = uiState.searchQuery,
                            lengthUnit = uiState.lengthUnit,
                            fabricRolls = uiState.fabricRolls,
                            onRefresh = onRefresh,
                            onSearchQueryChange = onSearchQueryChange,
                            onLengthUnitSelect = onLengthUnitSelect,
                            onFabricRollTableItemClick = onFabricRollTableItemClick,
                            onAddFabricRollClick = onAddFabricRollClick,
                            onEditFabricRollClick = onEditFabricRollClick,
                            onDeleteFabricRollClick = onDeleteFabricRollClick,
                            onOutboundFabricRollClick = onOutboundFabricRollClick,
                            modifier = Modifier
                                .fillMaxSize()
                                .hideKeyboardOnClick()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    isRefreshing: Boolean,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsTopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = { BackIcon(onClick = onBackClick) },
        actions = {
            RefreshIcon(
                isRefreshing = isRefreshing,
                onClick = onRefresh,
            )
        },
    )
}

@Composable
private fun ZoneDetailContent(
    zone: Zone,
    searchQuery: String,
    lengthUnit: LengthUnit,
    fabricRolls: UiResult<List<FabricRoll>>,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onLengthUnitSelect: (LengthUnit) -> Unit,
    onFabricRollTableItemClick: (FabricRoll) -> Unit,
    onAddFabricRollClick: (zoneId: Long) -> Unit,
    onEditFabricRollClick: (FabricRoll) -> Unit,
    onOutboundFabricRollClick: (FabricRoll) -> Unit,
    onDeleteFabricRollClick: (FabricRoll) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            ZoneNameHeader(name = zone.name)
            ZoneDetailDashboard(
                zone = zone,
                lengthUnit = lengthUnit,
            )
            SearchTextField(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
            )
            FabricRollsTableHeader(
                displayLengthUnit = lengthUnit,
                onLengthUnitSelect = onLengthUnitSelect,
                onAddFabricRollClick = { onAddFabricRollClick(zone.id) },
            )
            when (fabricRolls) {
                is UiResult.Error -> ErrorContent(
                    onRetryClick = onRefresh,
                    modifier = Modifier.weight(1f),
                )

                else -> FabricRollsTable(
                    fabricRolls = when (fabricRolls) {
                        is UiResult.Success -> fabricRolls.data
                        else -> emptyList()
                    },
                    lengthUnit = lengthUnit,
                    onFabricRollTableItemClick = onFabricRollTableItemClick,
                    onDeleteFabricRollClick = onDeleteFabricRollClick,
                    onEditFabricRollClick = onEditFabricRollClick,
                    onOutboundFabricRollClick = onOutboundFabricRollClick,
                    modifier = Modifier.heightIn(max = this@BoxWithConstraints.maxHeight - 32.dp),
                )
            }
        }
    }
}

@Composable
private fun ZoneNameHeader(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        style = SdsTheme.typography.headlineSmall,
        modifier = modifier,
    )
}

@Composable
private fun ZoneDetailDashboard(
    zone: Zone,
    lengthUnit: LengthUnit,
    modifier: Modifier = Modifier,
) {
    DashboardGrid(modifier = modifier) {
        DashboardCard(
            icon = vectorResource(Icons.Package),
            title = "원단 롤",
            content = zone.stats.rollCount.toString(),
            description = "구역 내 원단 롤",
            modifier = Modifier.weight(1f),
        )
        DashboardCard(
            icon = vectorResource(Icons.Edit),
            title = "총 수량",
            content = (zone.stats.totalQuantity * lengthUnit.multiplier).formatDecimal(1) + " ${lengthUnit.abbreviateName}",
            description = "재고 내 원단 수량",
            modifier = Modifier.weight(1f),
        )
        DashboardCard(
            icon = vectorResource(Icons.Calendar),
            title = "생성일",
            content = InstantFormatter.formatDottedDate(zone.createdAt),
            description = "구역 생성일",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsTextField(
        modifier = modifier,
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
}

@Composable
private fun FabricRollsTableHeader(
    displayLengthUnit: LengthUnit,
    onLengthUnitSelect: (LengthUnit) -> Unit,
    onAddFabricRollClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "표시 단위:")
        LengthUnitToggleButtons(
            selected = displayLengthUnit,
            onSelected = onLengthUnitSelect,
        )
        Spacer(Modifier.weight(1f))
        SdsButton(
            buttonVariant = ButtonVariant.Primary,
            buttonSize = ButtonSize.Small,
            onClick = onAddFabricRollClick,
            text = "원단 롤 추가",
            leadingIcon = vectorResource(Icons.Plus)
        )
    }
}

@Composable
private fun FabricRollsTable(
    fabricRolls: List<FabricRoll>,
    lengthUnit: LengthUnit,
    onFabricRollTableItemClick: (FabricRoll) -> Unit,
    onDeleteFabricRollClick: (FabricRoll) -> Unit,
    onEditFabricRollClick: (FabricRoll) -> Unit,
    onOutboundFabricRollClick: (FabricRoll) -> Unit,
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
