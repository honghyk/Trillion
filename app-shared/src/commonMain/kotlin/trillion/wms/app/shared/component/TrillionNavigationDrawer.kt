package trillion.wms.app.shared.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.designsystem.component.AppIcon
import trillion.wms.core.designsystem.component.SdsDivider
import trillion.wms.core.designsystem.component.SdsNavigationDrawerItem
import trillion.wms.core.designsystem.component.SdsPermanentDrawerSheet
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun TrillionNavigationDrawer(
    currentDestination: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsPermanentDrawerSheet(
        modifier = modifier,
    ) {
        NavigationDrawerHeader()
        SdsDivider(Modifier.padding(horizontal = 12.dp))
        Spacer(Modifier.height(8.dp))

        MainTab.entries.forEach { destination ->
            NavigationDrawerItem(
                tab = destination,
                selected = currentDestination == destination,
                onTabSelected = onTabSelected,
            )
        }
    }
}

@Composable
private fun NavigationDrawerHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppIcon(
            modifier = Modifier.size(32.dp),
            iconSize = 16.dp,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Trillion",
                style = SdsTheme.typography.bodyStrong,
            )
            Text(
                text = "원단 재고 관리 시스템",
                style = SdsTheme.typography.bodySmall,
                color = SdsTheme.colorScheme.textDefaultSecondary,
            )
        }
    }
}

@Composable
private fun NavigationDrawerItem(
    tab: MainTab,
    selected: Boolean,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsNavigationDrawerItem(
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
private fun TrillionNavigationDrawerPreview() {
    SdsTheme {
        TrillionNavigationDrawer(
            currentDestination = MainTab.INVENTORY,
            onTabSelected = {},
        )
    }
}
