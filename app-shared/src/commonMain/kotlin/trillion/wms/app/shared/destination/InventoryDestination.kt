package trillion.wms.app.shared.destination

import androidx.navigation.NavGraphBuilder
import trillion.wms.app.shared.component.mainTabComposable
import trillion.wms.app.shared.route.Inventory
import trillion.wms.feature.inventory.InventoryScreen

fun NavGraphBuilder.inventoryDestination(
    onFabricRollTableItemClick: (Long) -> Unit,
    onOutboundFabricRollClick: (Long) -> Unit,
    onEditFabricRollClick: (Long) -> Unit,
) {
    mainTabComposable<Inventory> {
        InventoryScreen(
            onFabricRollTableItemClick = onFabricRollTableItemClick,
            onOutboundFabricRollClick = onOutboundFabricRollClick,
            onEditFabricRollClick = onEditFabricRollClick,
        )
    }
}
