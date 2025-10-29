package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import trillion.wms.core.domain.utils.cancellableRunCatching
import kotlin.time.Duration.Companion.seconds

internal interface UserInitiatedParams {
    val isUserInitiated: Boolean
}

abstract class Interactor<in P, R> {
    private val loadingState = MutableStateFlow(LoadingState())

    val inProgress: Flow<Boolean> by lazy {
        loadingState
            .debounce {
                if (it.ambientCount > 0) {
                    5.seconds
                } else {
                    0.seconds
                }
            }
            .map { (it.userCount + it.ambientCount) > 0 }
            .distinctUntilChanged()
    }

    suspend operator fun invoke(
        params: P,
        userInitiated: Boolean = params.isUserInitiated,
    ): Result<R> = cancellableRunCatching {
        addLoader(userInitiated)
        doWork(params)
    }.also {
        removeLoader(userInitiated)
    }

    private val P.isUserInitiated: Boolean
        get() = (this as? UserInitiatedParams)?.isUserInitiated ?: true

    protected abstract suspend fun doWork(params: P): R

    private fun addLoader(fromUser: Boolean) {
        loadingState.update {
            if (fromUser) {
                it.copy(userCount = it.userCount + 1)
            } else {
                it.copy(ambientCount = it.ambientCount + 1)
            }
        }
    }

    private fun removeLoader(fromUser: Boolean) {
        loadingState.update {
            if (fromUser) {
                it.copy(userCount = it.userCount - 1)
            } else {
                it.copy(ambientCount = it.ambientCount - 1)
            }
        }
    }

    private data class LoadingState(
        val userCount: Int = 0,
        val ambientCount: Int = 0,
    )
}
