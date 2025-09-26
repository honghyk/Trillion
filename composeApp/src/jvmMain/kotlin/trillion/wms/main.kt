package trillion.wms

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import trillion.wms.app.shared.TrillionApp
import trillion.wms.app.shared.di.initKoin

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
