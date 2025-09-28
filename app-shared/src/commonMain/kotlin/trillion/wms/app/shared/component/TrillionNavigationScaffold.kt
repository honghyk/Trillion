package trillion.wms.app.shared.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import trillion.wms.core.designsystem.component.SdsDrawerDefaults
import trillion.wms.core.designsystem.component.SdsNavigationBarDefaults
import trillion.wms.core.designsystem.component.SdsNavigationRailDefaults
import trillion.wms.core.designsystem.component.SdsScaffold
import trillion.wms.core.ui.compositionlocal.LocalBottomNavigationBarsPadding

enum class NavigationBarType { NavigationDrawer, NavigationRail, BottomNavigation }

@Composable
fun TrillionNavigationScaffold(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val navigationBarType = remember(windowAdaptiveInfo) {
        with(windowAdaptiveInfo) {
            when {
                windowPosture.isTabletop || windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT ->
                    NavigationBarType.BottomNavigation

                windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM -> NavigationBarType.NavigationRail
                windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED -> NavigationBarType.NavigationDrawer
                else -> NavigationBarType.BottomNavigation
            }
        }
    }

    TrillionNavigationScaffold(
        currentDestination = currentTab,
        onTabSelected = onTabSelected,
        navigationBarType = navigationBarType,
        modifier = modifier,
        content = content,
    )
}

@Composable
private fun TrillionNavigationScaffold(
    currentDestination: MainTab,
    onTabSelected: (MainTab) -> Unit,
    navigationBarType: NavigationBarType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(modifier = modifier.fillMaxSize()) {
        when (navigationBarType) {
            NavigationBarType.NavigationDrawer -> {
                TrillionNavigationDrawer(
                    currentDestination = currentDestination,
                    onTabSelected = onTabSelected,
                    modifier = Modifier.windowInsetsPadding(SdsDrawerDefaults.windowInsets),
                )
            }

            NavigationBarType.NavigationRail -> {
                TrillionNavigationRail(
                    currentDestination = currentDestination,
                    onTabSelected = onTabSelected,
                    modifier = Modifier.windowInsetsPadding(SdsNavigationRailDefaults.windowInsets)
                )
            }

            NavigationBarType.BottomNavigation -> Unit
        }
        SdsScaffold(
            bottomBar = {
                if (navigationBarType == NavigationBarType.BottomNavigation) {
                    TrillionNavigationBar(
                        currentDestination = currentDestination,
                        onTabSelected = onTabSelected,
                        modifier = Modifier.windowInsetsPadding(SdsNavigationBarDefaults.windowInsets)
                    )
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { bottomNavigationBarsPadding ->
            CompositionLocalProvider(LocalBottomNavigationBarsPadding provides bottomNavigationBarsPadding) {
                content()
            }
        }
    }
}
