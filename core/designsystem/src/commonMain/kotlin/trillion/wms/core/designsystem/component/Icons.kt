package trillion.wms.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.vectorResource
import trillion.wms.core.designsystem.theme.SdsTheme
import trillionwms.core.designsystem.generated.resources.Res
import trillionwms.core.designsystem.generated.resources.ic_alert_circle
import trillionwms.core.designsystem.generated.resources.ic_arrow_back
import trillionwms.core.designsystem.generated.resources.ic_calendar
import trillionwms.core.designsystem.generated.resources.ic_check_circle
import trillionwms.core.designsystem.generated.resources.ic_chevron_down
import trillionwms.core.designsystem.generated.resources.ic_chevron_up
import trillionwms.core.designsystem.generated.resources.ic_close
import trillionwms.core.designsystem.generated.resources.ic_dashboard
import trillionwms.core.designsystem.generated.resources.ic_dashboard_filled
import trillionwms.core.designsystem.generated.resources.ic_edit
import trillionwms.core.designsystem.generated.resources.ic_filter
import trillionwms.core.designsystem.generated.resources.ic_map_pin
import trillionwms.core.designsystem.generated.resources.ic_map_pin_filled
import trillionwms.core.designsystem.generated.resources.ic_minus
import trillionwms.core.designsystem.generated.resources.ic_more_vertical
import trillionwms.core.designsystem.generated.resources.ic_package
import trillionwms.core.designsystem.generated.resources.ic_package_filled
import trillionwms.core.designsystem.generated.resources.ic_plus
import trillionwms.core.designsystem.generated.resources.ic_refresh_cw
import trillionwms.core.designsystem.generated.resources.ic_rotate_cw
import trillionwms.core.designsystem.generated.resources.ic_search
import trillionwms.core.designsystem.generated.resources.ic_trash_2
import trillionwms.core.designsystem.generated.resources.ic_trending_down
import trillionwms.core.designsystem.generated.resources.ic_trending_up

object Icons {
    val Close = Res.drawable.ic_close
    val ArrowBack = Res.drawable.ic_arrow_back
    val Search = Res.drawable.ic_search
    val Calendar = Res.drawable.ic_calendar
    val CheckCircle = Res.drawable.ic_check_circle
    val Plus = Res.drawable.ic_plus
    val Minus = Res.drawable.ic_minus
    val Trash = Res.drawable.ic_trash_2
    val Edit = Res.drawable.ic_edit
    val Filter = Res.drawable.ic_filter
    val Dashboard = Res.drawable.ic_dashboard
    val MapPin = Res.drawable.ic_map_pin
    val Package = Res.drawable.ic_package
    val TrendingDown = Res.drawable.ic_trending_down
    val TrendingUp = Res.drawable.ic_trending_up
    val MoreVertical = Res.drawable.ic_more_vertical
    val ChevronDown = Res.drawable.ic_chevron_down
    val ChevronUp = Res.drawable.ic_chevron_up
    val AlertCircle = Res.drawable.ic_alert_circle
    val RefreshCw = Res.drawable.ic_refresh_cw
    val RotateCw = Res.drawable.ic_rotate_cw

    object Filled {
        val Dashboard = Res.drawable.ic_dashboard_filled
        val MapPin = Res.drawable.ic_map_pin_filled
        val Package = Res.drawable.ic_package_filled
    }
}

@Composable
fun AppIcon(
    iconSize: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = SdsTheme.colorScheme.backgroundAccentDefault,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = vectorResource(Icons.Package),
                contentDescription = null,
            )
        }
    }
}
