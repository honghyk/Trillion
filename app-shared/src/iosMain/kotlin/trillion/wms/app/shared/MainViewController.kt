package trillion.wms.app.shared

import androidx.compose.ui.window.ComposeUIViewController
import trillion.wms.app.shared.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { TrillionApp() }
