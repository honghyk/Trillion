package trillion.wms.feature.zone.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import trillion.wms.core.designsystem.component.DropdownIcon
import trillion.wms.core.designsystem.component.Icons
import trillion.wms.core.designsystem.component.SdsCard
import trillion.wms.core.designsystem.component.SdsDivider
import trillion.wms.core.designsystem.component.SdsDropdownMenuItem
import trillion.wms.core.designsystem.component.SdsScaffold
import trillion.wms.core.designsystem.component.SdsTopAppBar
import trillion.wms.core.designsystem.theme.SdsTheme
import trillion.wms.core.model.Zone
import trillion.wms.core.ui.component.EmptyContent
import trillion.wms.core.ui.component.RefreshableContent
import trillion.wms.core.ui.component.UiResultContent
import trillion.wms.core.ui.compositionlocal.safeDrawingWithBottomNavBar
import trillion.wms.core.ui.utils.InstantFormatter

@Composable
fun ZoneListScreen(
    onZoneItemClick: (Long) -> Unit,
    onAddZoneClick: () -> Unit,
    viewModel: ZoneListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ZoneListScreen(
        uiState = uiState,
        onZoneClick = { onZoneItemClick(it.id) },
        onCreateZoneClick = onAddZoneClick,
        onDeleteClick = viewModel::deleteZone,
        onRefresh = viewModel::refresh,
        onMessageShown = viewModel::clearMessage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneListScreen(
    uiState: ZoneListUiState,
    onZoneClick: (Zone) -> Unit,
    onCreateZoneClick: () -> Unit,
    onDeleteClick: (Zone) -> Unit,
    onRefresh: () -> Unit,
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
            ZoneListAppBar(
                isRefreshing = uiState.isRefreshing,
                onCreateZoneClick = onCreateZoneClick,
                onRefresh = onRefresh,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawingWithBottomNavBar,
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
            UiResultContent(
                uiResult = uiState.zones,
                isRefreshing = uiState.isRefreshing,
                onRetry = onRefresh,
            ) { zones ->
                RefreshableContent(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = onRefresh,
                ) {
                    when {
                        zones.isEmpty() -> EmptyContent(
                            onCreateZoneClick = onCreateZoneClick
                        )

                        else -> ZoneListContent(
                            zones = zones,
                            onZoneClick = onZoneClick,
                            onDeleteClick = onDeleteClick,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneListAppBar(
    isRefreshing: Boolean,
    onCreateZoneClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsTopAppBar(
        title = { Text(text = "구역") },
        actions = {
            RefreshIcon(
                isRefreshing = isRefreshing,
                onClick = onRefresh,
            )
            ActionIcon(
                imageVector = vectorResource(Icons.Plus),
                onClick = onCreateZoneClick,
                contentDescription = "구역 생성",
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun EmptyContent(
    onCreateZoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyContent(
        icon = vectorResource(Icons.MapPin),
        text = "아직 생성된 구역이 없습니다",
        subText = "원단 롤 관리를 위해 보관 구역을 생성 해주세요",
        buttonText = "첫 구역 생성",
        buttonLeadingIcon = vectorResource(Icons.Plus),
        onButtonClick = onCreateZoneClick,
        modifier = modifier,
    )
}

@Composable
private fun ZoneListContent(
    zones: List<Zone>,
    onZoneClick: (Zone) -> Unit,
    onDeleteClick: (Zone) -> Unit,
    modifier: Modifier = Modifier
) {
    ZoneCardsGrid(
        zones = zones,
        onCardClick = onZoneClick,
        onDeleteZoneClick = onDeleteClick,
        modifier = modifier,
    )
}

@Composable
private fun ZoneCardsGrid(
    zones: List<Zone>,
    onCardClick: (Zone) -> Unit,
    onDeleteZoneClick: (Zone) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 480.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 24.dp),
    ) {
        items(
            count = zones.size,
            key = { index -> zones[index].id }
        ) { index ->
            ZoneCard(
                zone = zones[index],
                onClick = { onCardClick(zones[index]) },
                onDeleteClick = { onDeleteZoneClick(zones[index]) },
            )
        }
    }
}

@Composable
private fun ZoneCard(
    zone: Zone,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = zone.name,
                        style = SdsTheme.typography.bodyStrong,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (zone.description.isNotBlank()) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = zone.description,
                            style = SdsTheme.typography.bodySmall,
                            color = SdsTheme.colorScheme.textDefaultSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                ZoneActionsDropdown(onDeleteClick = onDeleteClick)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = vectorResource(Icons.Package),
                    contentDescription = "Rolls",
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = "${zone.metrics.rollCount} 롤",
                )
            }

            Column {
                SdsDivider()
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "생성일: ${InstantFormatter.formatDottedDate(zone.createdAt)}",
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneActionsDropdown(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    DropdownIcon(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        icon = {
            Icon(
                imageVector = vectorResource(Icons.MoreVertical),
                contentDescription = "More Options",
                modifier = Modifier.size(16.dp)
            )
        },
        menuItems = {
            SdsDropdownMenuItem(
                text = { Text("삭제") },
                onClick = {
                    expanded = false
                    onDeleteClick()
                }
            )
        }
    )
}
