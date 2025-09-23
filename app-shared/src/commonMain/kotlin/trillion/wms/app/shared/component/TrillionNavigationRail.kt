package trillion.wms.app.shared.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.designsystem.component.AppIcon
import trillion.wms.core.designsystem.component.SdsDivider
import trillion.wms.core.designsystem.component.SdsNavigationRail
import trillion.wms.core.designsystem.component.SdsNavigationRailItem
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun TrillionNavigationRail(
    currentDestination: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsNavigationRail(
        modifier = modifier,
    ) {
        NavigationRailHeader()
        SdsDivider(modifier = Modifier.padding(vertical = 12.dp))
        MainTab.entries.forEach { destination ->
            NavigationRailItem(
                tab = destination,
                selected = currentDestination == destination,
                onTabSelected = onTabSelected,
            )
        }
    }
}

@Composable
private fun NavigationRailHeader(
    modifier: Modifier = Modifier,
) {
    AppIcon(
        modifier = modifier.size(40.dp),
        iconSize = 20.dp,
    )
}

@Composable
private fun NavigationRailItem(
    tab: MainTab,
    selected: Boolean,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsNavigationRailItem(
        modifier = modifier,
        selected = selected,
        onClick = { onTabSelected(tab) },
        icon = {
            val iconResource = if (selected) tab.selectedIcon else tab.unselectedIcon
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(iconResource),
                contentDescription = null,
            )
        }
    )
}

@Composable
@Preview
private fun TrillionNavigationRailPreview() {
    SdsTheme {
        TrillionNavigationRail(
            currentDestination = MainTab.ZONES,
            onTabSelected = {},
        )
    }
}
