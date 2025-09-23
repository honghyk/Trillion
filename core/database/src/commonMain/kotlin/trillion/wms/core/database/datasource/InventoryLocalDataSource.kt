package trillion.wms.core.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import trillion.wms.core.model.InventorySummary

interface InventoryLocalDataSource {
    fun getInventorySummary(): Flow<InventorySummary?>
    suspend fun setInventorySummary(summary: InventorySummary)
}

class InMemoryInventoryLocalDataSource : InventoryLocalDataSource {
    private val summaryFlow = MutableStateFlow<InventorySummary?>(null)

    override fun getInventorySummary(): Flow<InventorySummary?> = summaryFlow

    override suspend fun setInventorySummary(summary: InventorySummary) {
        summaryFlow.value = summary
    }
}
