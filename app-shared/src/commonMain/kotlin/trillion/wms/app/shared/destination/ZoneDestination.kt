package trillion.wms.app.shared.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import trillion.wms.app.shared.component.mainTabComposable
import trillion.wms.app.shared.route.ZoneDetail
import trillion.wms.app.shared.route.ZoneForm
import trillion.wms.app.shared.route.ZoneList
import trillion.wms.feature.zone.detail.ZoneDetailScreen
import trillion.wms.feature.zone.form.ZoneFormDialog
import trillion.wms.feature.zone.list.ZoneListScreen

fun NavGraphBuilder.zoneDestinations(
    onBackClick: () -> Unit,
    onZoneItemClick: (Long) -> Unit,
    onAddZoneClick: () -> Unit,
    onEditZoneClick: (Long) -> Unit,
    onAddFabricRollClick: (Long) -> Unit,
    onEditFabricRollClick: (Long) -> Unit,
    onOutboundFabricRollClick: (Long) -> Unit,
    onFabricRollTableItemClick: (Long) -> Unit,
) {
    zoneListDestination(
        onZoneItemClick = onZoneItemClick,
        onAddZoneClick = onAddZoneClick,
        onEditZoneClick = onEditZoneClick,
    )

    zoneDetailDestination(
        onBackClick = onBackClick,
        onAddFabricRollClick = onAddFabricRollClick,
        onEditFabricRollClick = onEditFabricRollClick,
        onOutboundFabricRollClick = onOutboundFabricRollClick,
        onFabricRollTableItemClick = onFabricRollTableItemClick,
    )

    zoneFormDialogDestination(
        onDismiss = onBackClick,
    )
}

fun NavGraphBuilder.zoneListDestination(
    onZoneItemClick: (Long) -> Unit,
    onAddZoneClick: () -> Unit,
    onEditZoneClick: (Long) -> Unit,
) {
    mainTabComposable<ZoneList> {
        ZoneListScreen(
            onZoneItemClick = onZoneItemClick,
            onAddZoneClick = onAddZoneClick,
            onEditZoneClick = onEditZoneClick,
        )
    }
}

fun NavGraphBuilder.zoneDetailDestination(
    onBackClick: () -> Unit,
    onAddFabricRollClick: (Long) -> Unit,
    onEditFabricRollClick: (Long) -> Unit,
    onOutboundFabricRollClick: (Long) -> Unit,
    onFabricRollTableItemClick: (Long) -> Unit,
) {
    composable<ZoneDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<ZoneDetail>()

        ZoneDetailScreen(
            zoneId = args.zoneId,
            onBackClick = onBackClick,
            onAddFabricRollClick = onAddFabricRollClick,
            onEditFabricRollClick = onEditFabricRollClick,
            onOutboundFabricRollClick = onOutboundFabricRollClick,
            onFabricRollTableItemClick = onFabricRollTableItemClick,
        )
    }
}

fun NavGraphBuilder.zoneFormDialogDestination(
    onDismiss: () -> Unit,
) {
    dialog<ZoneForm> { backStackEntry ->
        val args = backStackEntry.toRoute<ZoneForm>()

        ZoneFormDialog(
            zoneId = args.zoneId,
            onDismiss = onDismiss,
        )
    }
}
