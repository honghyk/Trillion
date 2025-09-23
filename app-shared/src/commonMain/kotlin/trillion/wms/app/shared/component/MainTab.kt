package trillion.wms.app.shared.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import org.jetbrains.compose.resources.DrawableResource
import trillion.wms.app.shared.route.BaseInventory
import trillion.wms.app.shared.route.BaseZoneList
import trillion.wms.app.shared.transition.materialFadeIn
import trillion.wms.app.shared.transition.materialFadeOut
import trillion.wms.core.designsystem.component.Icons
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType

enum class MainTab(
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val label: String,
    val route: KClass<*>,
) {
    ZONES(
        selectedIcon = Icons.Filled.MapPin,
        unselectedIcon = Icons.MapPin,
        label = "구역",
        route = BaseZoneList::class
    ),
    INVENTORY(
        selectedIcon = Icons.Filled.Package,
        unselectedIcon = Icons.Package,
        label = "재고",
        route = BaseInventory::class
    ),
}

inline fun <reified T : Any> NavGraphBuilder.mainTabComposable(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = { materialFadeIn() },
        exitTransition = { materialFadeOut() },
        popEnterTransition = { materialFadeIn() },
        popExitTransition = { materialFadeOut() },
        content = content,
    )
}
