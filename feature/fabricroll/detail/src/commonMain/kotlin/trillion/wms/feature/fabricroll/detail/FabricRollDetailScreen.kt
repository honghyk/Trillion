package trillion.wms.feature.fabricroll.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import trillion.wms.core.ui.component.ErrorContent
import trillion.wms.core.ui.component.LengthUnitToggleButtons
import trillion.wms.core.ui.component.LoadingContent
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.InstantFormatter
import trillion.wms.core.ui.utils.UiResult
import trillion.wms.core.ui.utils.formatDecimal
import trillion.wms.core.designsystem.component.ButtonSize
import trillion.wms.core.designsystem.component.ButtonVariant
import trillion.wms.core.designsystem.component.ColumnWidth
import trillion.wms.core.designsystem.component.DashboardCard
import trillion.wms.core.designsystem.component.DashboardGrid
import trillion.wms.core.designsystem.component.Icons
import trillion.wms.core.designsystem.component.ScrollableTable
import trillion.wms.core.designsystem.component.SdsButton
import trillion.wms.core.designsystem.component.SdsCard
import trillion.wms.core.designsystem.component.SdsOutlineButton
import trillion.wms.core.designsystem.component.SdsScaffold
import trillion.wms.core.designsystem.component.SdsTopAppBar
import trillion.wms.core.designsystem.theme.SdsTheme
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.model.Zone
import trillion.wms.core.model.outboundQuantity
import trillion.wms.core.ui.component.RefreshableContent
import trillion.wms.core.ui.compositionlocal.safeDrawingWithBottomNavBar
import trillion.wms.core.ui.utils.dataOrNull

@Composable
fun FabricRollDetailScreen(
    rollId: Long,
    onBackClick: () -> Unit,
    onOutboundFabricRollClick: (Long) -> Unit,
    onEditFabricRollClick: (Long) -> Unit,
    viewModel: FabricRollDetailViewModel = koinViewModel { parametersOf(rollId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FabricRollDetailScreen(
        uiState = uiState,
        onNavigateUp = onBackClick,
        onRefresh = { viewModel.refresh(true) },
        onLengthUnitSelected = viewModel::updateLengthUnit,
        onEditFabricRollClick = { roll -> onEditFabricRollClick(roll.id) },
        onOutboundFabricRollClick = { roll -> onOutboundFabricRollClick(roll.id) },
        onDeleteOutboundHistoryClick = viewModel::deleteOutboundHistory,
        onMessageShown = viewModel::clearMessage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FabricRollDetailScreen(
    uiState: FabricRollDetailUiState,
    onNavigateUp: () -> Unit,
    onRefresh: () -> Unit,
    onLengthUnitSelected: (LengthUnit) -> Unit,
    onEditFabricRollClick: (FabricRoll) -> Unit,
    onOutboundFabricRollClick: (FabricRoll) -> Unit,
    onDeleteOutboundHistoryClick: (OutboundHistory) -> Unit,
    onMessageShown: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    if (uiState.message != null) {
        LaunchedEffect(uiState.message) {
            snackbarHostState.showSnackbar(uiState.message.message)
            onMessageShown(uiState.message.id)
        }
    }

    SdsScaffold(
        modifier = modifier,
        topBar = {
            FabricRollDetailAppBar(
                isRefreshing = uiState.isRefreshing,
                onBackClick = onNavigateUp,
                onRefresh = onRefresh,
                onEditClick = {
                    val fabricRoll = uiState.fabricRoll.dataOrNull()
                    if (fabricRoll != null) {
                        onEditFabricRollClick(fabricRoll)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawingWithBottomNavBar
            .exclude(WindowInsets.ime),
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState.fabricRoll) {
                is UiResult.Loading -> LoadingContent()
                is UiResult.Error -> ErrorContent(onRetryClick = onRefresh)

                is UiResult.Success -> {
                    RefreshableContent(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = onRefresh,
                    ) {
                        FabricRollDetailContent(
                            zone = uiState.zone,
                            fabricRoll = uiState.fabricRoll.data,
                            lengthUnit = uiState.lengthUnit,
                            outboundHistories = uiState.outboundHistories,
                            onRefresh = onRefresh,
                            onLengthUnitSelected = onLengthUnitSelected,
                            onOutboundFabricRoll = onOutboundFabricRollClick,
                            onDeleteOutboundHistory = onDeleteOutboundHistoryClick,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FabricRollDetailAppBar(
    isRefreshing: Boolean,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
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
            ActionIcon(
                imageVector = vectorResource(Icons.Edit),
                onClick = onEditClick,
                contentDescription = null,
            )
        }
    )
}

@Composable
private fun FabricRollDetailContent(
    zone: Zone,
    fabricRoll: FabricRoll,
    lengthUnit: LengthUnit,
    outboundHistories: UiResult<List<OutboundHistory>>,
    onRefresh: () -> Unit,
    onLengthUnitSelected: (LengthUnit) -> Unit,
    onOutboundFabricRoll: (FabricRoll) -> Unit,
    onDeleteOutboundHistory: (OutboundHistory) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            FabricRollDetailHeader(
                zoneName = zone.name,
                itemNo = fabricRoll.itemNo,
                orderNo = fabricRoll.orderNo,
            )
            FabricRollDetailDashboard(
                fabricRoll = fabricRoll,
                lengthUnit = lengthUnit,
            )
            FabricRollInfoCard(
                fabricRoll = fabricRoll,
                lengthUnit = lengthUnit,
                onDisplayLengthUnitChange = onLengthUnitSelected,
            )
            OutboundTableHeader(
                onOutboundFabricRoll = { onOutboundFabricRoll(fabricRoll) }
            )
            when (outboundHistories) {
                is UiResult.Error -> ErrorContent(
                    onRetryClick = onRefresh,
                    modifier = Modifier.weight(1f),
                )

                else -> OutboundHistoryTable(
                    outboundHistories = when (outboundHistories) {
                        is UiResult.Success -> outboundHistories.data
                        else -> emptyList()
                    },
                    lengthUnit = lengthUnit,
                    onDeleteHistory = onDeleteOutboundHistory,
                    modifier = Modifier.heightIn(max = this@BoxWithConstraints.maxHeight - 32.dp),
                )
            }
        }
    }
}

@Composable
private fun FabricRollDetailHeader(
    zoneName: String,
    itemNo: String,
    orderNo: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = itemNo,
            style = SdsTheme.typography.headlineSmall,
        )
        Text(
            text = "위치: $zoneName" + " • " + "Order: ${orderNo ?: "-"}",
            color = SdsTheme.colorScheme.textDefaultSecondary,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FabricRollDetailDashboard(
    fabricRoll: FabricRoll,
    lengthUnit: LengthUnit,
    modifier: Modifier = Modifier,
) {
    DashboardGrid(modifier = modifier) {
        DashboardCard(
            icon = vectorResource(Icons.Package),
            title = "현재 재고",
            content = (fabricRoll.remainingQuantity * lengthUnit.multiplier).formatDecimal(1) + " " + lengthUnit.abbreviateName,
            modifier = Modifier.weight(1f),
        )
        DashboardCard(
            icon = vectorResource(Icons.TrendingDown),
            title = "초기 재고",
            content = (fabricRoll.quantity * lengthUnit.multiplier).formatDecimal(1) + " " + lengthUnit.abbreviateName,
            modifier = Modifier.weight(1f),
        )
        DashboardCard(
            icon = vectorResource(Icons.TrendingDown),
            title = "총 출고량",
            content = (fabricRoll.outboundQuantity * lengthUnit.multiplier).formatDecimal(1) + " " + lengthUnit.abbreviateName,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun FabricRollInfoCard(
    fabricRoll: FabricRoll,
    lengthUnit: LengthUnit,
    onDisplayLengthUnitChange: (LengthUnit) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    style = SdsTheme.typography.bodyStrong,
                    text = "원단 롤 세부사항",
                )
                LengthUnitToggleButtons(
                    selected = lengthUnit,
                    onSelected = onDisplayLengthUnitChange
                )
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                maxItemsInEachRow = 3,
            ) {
                DetailItemWithIcon(
                    icon = vectorResource(Icons.Package),
                    label = "Color",
                    value = fabricRoll.color.ifEmpty { "-" }
                )
                DetailItemWithIcon(
                    icon = vectorResource(Icons.Package),
                    label = "Factory",
                    value = fabricRoll.factory.ifEmpty { "-" }
                )
                DetailItemWithIcon(
                    icon = vectorResource(Icons.Package),
                    label = "Finish",
                    value = fabricRoll.finish.ifEmpty { "-" }
                )
                DetailItemWithIcon(
                    icon = vectorResource(Icons.Calendar),
                    label = "추가일",
                    value = InstantFormatter.formatDottedDate(fabricRoll.createdAt)
                )
            }
            RemarkItem(remark = fabricRoll.remark)
        }
    }
}

@Composable
private fun DetailItemWithIcon(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.widthIn(min = 280.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = SdsTheme.colorScheme.iconDefaultSecondary
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = SdsTheme.typography.bodySmall,
                color = SdsTheme.colorScheme.textDefaultSecondary
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun RemarkItem(
    remark: String?,
    modifier: Modifier = Modifier,
) {
    Column {
        Text(
            text = "비고",
            style = SdsTheme.typography.bodySmall,
            color = SdsTheme.colorScheme.textDefaultSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier
                .fillMaxWidth()
                .background(
                    color = SdsTheme.colorScheme.backgroundDefaultSecondary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
        ) {
            Text(text = remark ?: "-")
        }
    }
}

@Composable
private fun OutboundTableHeader(
    onOutboundFabricRoll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            style = SdsTheme.typography.bodyStrong,
            text = "출고 내역"
        )
        SdsButton(
            buttonVariant = ButtonVariant.Primary,
            buttonSize = ButtonSize.Small,
            onClick = onOutboundFabricRoll,
            text = "출고 처리",
            leadingIcon = vectorResource(Icons.Minus)
        )
    }
}

@Composable
private fun OutboundHistoryTable(
    outboundHistories: List<OutboundHistory>,
    lengthUnit: LengthUnit,
    onDeleteHistory: (OutboundHistory) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScrollableTable(
        modifier = modifier,
        columnWidths = List(6) { ColumnWidth.MaxIntrinsicWidth },
        rows = outboundHistories,
        headerRowContent = { column ->
            when (column) {
                0 -> HeaderCell(text = "No")
                1 -> HeaderCell(text = "날짜")
                2 -> HeaderCell(text = "출고 수량 (${lengthUnit.unitName})")
                3 -> HeaderCell(text = "Buyer")
                4 -> HeaderCell(text = "비고")
                5 -> HeaderCell(text = "")
            }
        },
        dataRowContent = { history, column, row ->
            when (column) {
                0 -> TextCell(text = (row + 1).toString())
                1 -> TextCell(text = InstantFormatter.formatDottedDate(history.createdAt))
                2 -> TextCell(text = (history.quantity * lengthUnit.multiplier).formatDecimal(1))
                3 -> TextCell(text = history.buyer)
                4 -> TextCell(
                    modifier = Modifier.widthIn(max = 200.dp),
                    text = history.remark.ifEmpty { "-" }
                )

                5 -> DataRowCell {
                    SdsOutlineButton(
                        buttonVariant = ButtonVariant.Primary,
                        leadingIcon = vectorResource(Icons.Trash),
                        text = "삭제",
                        onClick = { onDeleteHistory(history) }
                    )
                }
            }
        }
    )
}
