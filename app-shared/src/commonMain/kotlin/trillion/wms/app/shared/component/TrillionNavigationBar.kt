package trillion.wms.app.shared.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.designsystem.component.SdsNavigationBar
import trillion.wms.core.designsystem.component.SdsNavigationBarItem
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun TrillionNavigationBar(
    currentDestination: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsNavigationBar(
        modifier = modifier,
    ) {
        MainTab.entries.forEach { destination ->
            TrillionNavigationBarItem(
                tab = destination,
                selected = currentDestination == destination,
                onTabSelected = onTabSelected,
            )
        }
    }
}

@Composable
private fun RowScope.TrillionNavigationBarItem(
    tab: MainTab,
    selected: Boolean,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsNavigationBarItem(
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
        },
        label = { Text(text = tab.label) }
    )
}

@Composable
@Preview
private fun TrillionNavigationBarPreview() {
    SdsTheme {
        TrillionNavigationBar(
            currentDestination = MainTab.INVENTORY,
            onTabSelected = {},
        )
    }
}
