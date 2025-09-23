package trillion.wms.app.shared.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import trillion.wms.app.shared.route.FabricRollDetail
import trillion.wms.feature.fabricroll.detail.FabricRollDetailScreen

fun NavGraphBuilder.fabricRollDetailDestination(
    onBackClick: () -> Unit,
    onOutboundFabricRollClick: (Long) -> Unit,
    onEditFabricRollClick: (Long, Long) -> Unit,
) {
    composable<FabricRollDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<FabricRollDetail>()

        FabricRollDetailScreen(
            rollId = args.rollId,
            onBackClick = onBackClick,
            onOutboundFabricRollClick = onOutboundFabricRollClick,
            onEditFabricRollClick = onEditFabricRollClick,
        )
    }
}
