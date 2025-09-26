package trillion.wms.core.ui.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UiMessage(
    val message: String,
    val id: Long = Uuid.random().mostSignificantBits(),
)

class UiMessageManager() {
    private val _messages = MutableStateFlow<List<UiMessage>>(emptyList())

    val message: Flow<UiMessage?> = _messages.map { it.firstOrNull() }.distinctUntilChanged()

    fun emitMessage(message: UiMessage) {
        _messages.update { it + message }
    }

    fun emitMessage(message: String) {
        _messages.update { it + UiMessage(message) }
    }

    fun clearMessage(id: Long) {
        _messages.update { messages ->
            messages.filterNot { it.id == id }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
private fun Uuid.mostSignificantBits(): Long =
    toLongs { mostSignificantBits, leastSignificantBits -> mostSignificantBits }
