package trillion.wms.app.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import trillion.wms.app.shared.component.MainTab
import trillion.wms.app.shared.component.NavHostWithSharedXAxis
import trillion.wms.app.shared.destination.fabricRollDetailDestination
import trillion.wms.app.shared.destination.fabricRollFormDestination
import trillion.wms.app.shared.destination.fabricRollOutboundFormDialogDestination
import trillion.wms.app.shared.destination.inventoryDestination
import trillion.wms.app.shared.destination.zoneDestinations
import trillion.wms.app.shared.route.BaseInventory
import trillion.wms.app.shared.route.BaseZoneList
import trillion.wms.app.shared.route.FabricRollDetail
import trillion.wms.app.shared.route.FabricRollForm
import trillion.wms.app.shared.route.Inventory
import trillion.wms.app.shared.route.OutboundForm
import trillion.wms.app.shared.route.ZoneDetail
import trillion.wms.app.shared.route.ZoneForm
import trillion.wms.app.shared.route.ZoneList

@Composable
fun TrillionNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHostWithSharedXAxis(
        modifier = modifier,
        navController = navController,
        startDestination = MainTab.ZONES.route,
    ) {
        navigation<BaseZoneList>(
            startDestination = ZoneList::class
        ) {
            zoneDestinations(
                onBackClick = { navController.popBackStack() },
                onZoneItemClick = { navController.navigate(ZoneDetail(it)) },
                onAddZoneClick = { navController.navigate(ZoneForm(null)) },
                onEditZoneClick = { zoneId ->
                    navController.navigate(ZoneForm(zoneId))
                },
                onAddFabricRollClick = { zoneId ->
                    navController.navigate(FabricRollForm.add(zoneId))
                },
                onEditFabricRollClick = { rollId ->
                    navController.navigate(FabricRollForm.edit(rollId))
                },
                onOutboundFabricRollClick = { navController.navigate(OutboundForm(it)) },
                onFabricRollTableItemClick = { navController.navigate(FabricRollDetail(it)) },
            )
            fabricRollDetailDestination(
                onBackClick = { navController.popBackStack() },
                onOutboundFabricRollClick = { navController.navigate(OutboundForm(it)) },
                onEditFabricRollClick = { rollId ->
                    navController.navigate(FabricRollForm.edit(rollId))
                },
            )
        }

        navigation<BaseInventory>(
            startDestination = Inventory::class
        ) {
            inventoryDestination(
                onFabricRollTableItemClick = { navController.navigate(FabricRollDetail(it)) },
                onOutboundFabricRollClick = { navController.navigate(OutboundForm(it)) },
                onEditFabricRollClick = { rollId ->
                    navController.navigate(FabricRollForm.edit(rollId))
                },
            )
            fabricRollDetailDestination(
                onBackClick = { navController.popBackStack() },
                onOutboundFabricRollClick = { navController.navigate(OutboundForm(it)) },
                onEditFabricRollClick = { rollId ->
                    navController.navigate(FabricRollForm.edit(rollId))
                },
            )
        }

        fabricRollFormDestination(
            onDismiss = { navController.popBackStack() },
        )

        fabricRollOutboundFormDialogDestination(
            onDismiss = { navController.popBackStack() },
        )
    }
}
