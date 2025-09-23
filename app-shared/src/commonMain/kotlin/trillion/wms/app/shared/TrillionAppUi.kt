package trillion.wms.app.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import trillion.wms.app.shared.component.MainTab
import trillion.wms.app.shared.component.TrillionNavigationScaffold
import trillion.wms.app.shared.route.BaseInventory
import trillion.wms.app.shared.route.BaseZoneList

@Composable
fun TrillionAppUi() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentTab = MainTab.entries.firstOrNull { tab ->
        navBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(tab.route) } == true
    } ?: MainTab.ZONES

    TrillionNavigationScaffold(
        currentTab = currentTab,
        onTabSelected = { navController.navigateToTopLevelDestination(it) },
    ) {
        TrillionNavHost(
            navController = navController,
            modifier = Modifier,
        )
    }
}

fun NavController.navigateToTopLevelDestination(tab: MainTab) {
    val navOptions = navOptions {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
    when (tab) {
        MainTab.ZONES -> navigate(BaseZoneList, navOptions)
        MainTab.INVENTORY -> navigate(BaseInventory, navOptions)
    }
}
