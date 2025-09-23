package trillion.wms.app.shared.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import trillion.wms.app.shared.route.OutboundForm
import trillion.wms.feature.outbound.OutboundFormDialog

fun NavGraphBuilder.fabricRollOutboundFormDialogDestination(
    onDismiss: () -> Unit,
) {
    dialog<OutboundForm> { backStackEntry ->
        val args = backStackEntry.toRoute<OutboundForm>()

        OutboundFormDialog(
            rollId = args.rollId,
            onDismiss = onDismiss,
        )
    }
}
