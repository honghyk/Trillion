package trillion.wms.app.shared.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import trillion.wms.app.shared.route.FabricRollForm
import trillion.wms.feature.fabricroll.form.FabricRollFormDialog

fun NavGraphBuilder.fabricRollFormDialogDestination(
    onDismiss: () -> Unit,
) {
    dialog<FabricRollForm> { backStackEntry ->
        val args = backStackEntry.toRoute<FabricRollForm>()

        FabricRollFormDialog(
            zoneId = args.zoneId,
            rollId = args.rollId,
            onDismiss = onDismiss,
        )
    }
}
