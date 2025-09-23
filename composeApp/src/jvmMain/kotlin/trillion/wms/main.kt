package trillion.wms

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import trillion.wms.app.shared.TrillionApp
import trillion.wms.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Trillion",
        ) {
            TrillionApp()
        }
    }
}
