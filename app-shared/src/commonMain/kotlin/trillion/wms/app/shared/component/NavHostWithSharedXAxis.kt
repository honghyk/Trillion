package trillion.wms.app.shared.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import trillion.wms.app.shared.transition.materialSharedAxisXIn
import trillion.wms.app.shared.transition.materialSharedAxisXOut
import trillion.wms.app.shared.transition.rememberSlideDistance
import kotlin.reflect.KClass

@Composable
fun NavHostWithSharedXAxis(
    navController: NavHostController,
    startDestination: KClass<*>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    route: KClass<*>? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    val slideDistance = rememberSlideDistance()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        contentAlignment = contentAlignment,
        route = route,
        enterTransition = {
            materialSharedAxisXIn(
                forward = true,
                slideDistance = slideDistance,
            )
        },
        exitTransition = {
            materialSharedAxisXOut(
                forward = true,
                slideDistance = slideDistance,
            )
        },
        popEnterTransition = {
            materialSharedAxisXIn(
                forward = false,
                slideDistance = slideDistance,
            )
        },
        popExitTransition = {
            materialSharedAxisXOut(
                forward = false,
                slideDistance = slideDistance,
            )
        },
        builder = builder
    )
}
