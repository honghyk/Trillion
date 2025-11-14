package trillion.wms

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.painterResource
import trillion.wms.app.shared.TrillionApp
import trillion.wms.app.shared.di.initKoin
import trillionwms.desktop_app.generated.resources.AppIcon
import trillionwms.desktop_app.generated.resources.Res

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            icon = painterResource(Res.drawable.AppIcon),
            title = "Trillion",
        ) {
            TrillionApp()
        }
    }
}
