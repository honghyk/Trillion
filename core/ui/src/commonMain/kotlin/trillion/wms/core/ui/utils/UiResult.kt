package trillion.wms.core.ui.utils

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed interface UiResult<out T> {
    data object Loading : UiResult<Nothing>
    data class Error(val exception: Throwable) : UiResult<Nothing>
    data class Success<T>(val data: T) : UiResult<T>
}

fun <T> Flow<T>.asUiResult(): Flow<UiResult<T>> {
    return this
        .map { UiResult.Success(it) as UiResult<T> }
        .catch {
            Logger.d { "$it" }
            emit(UiResult.Error(it))
        }
}

fun <T> UiResult<T>.dataOrNull(): T? = (this as? UiResult.Success)?.data

class RefreshableUiResultFlow<T>(
    produce: () -> Flow<T>
) {
    private val refreshTrigger = MutableStateFlow(0)
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val flow: Flow<UiResult<T>> = refreshTrigger.flatMapLatest {
        produce()
            .asUiResult()
            .onEach { _isRefreshing.value = false }
    }

    fun refresh() {
        refreshTrigger.value++
        _isRefreshing.value = true
    }
}
