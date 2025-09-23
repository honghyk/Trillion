package trillion.wms.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun <T> UiSideEffectHandler(
    effect: T?,
    onConsumed: () -> Unit,
    block: suspend (T) -> Unit,
) {
    LaunchedEffect(effect) {
        if (effect != null) {
            block(effect)
            onConsumed()
        }
    }
}
