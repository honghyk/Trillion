package trillion.wms.app.shared.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import trillion.wms.app.shared.route.FabricRollForm
import trillion.wms.feature.fabricroll.form.FabricRollFormScreen

fun NavGraphBuilder.fabricRollFormDestination(
    onDismiss: () -> Unit,
) {
    composable<FabricRollForm> { backStackEntry ->
        val args = backStackEntry.toRoute<FabricRollForm>()

        FabricRollFormScreen(
            zoneId = args.zoneId,
            rollId = args.rollId,
            onDismiss = onDismiss,
        )
    }
}
