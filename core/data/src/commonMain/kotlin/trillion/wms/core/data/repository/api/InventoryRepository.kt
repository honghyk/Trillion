package trillion.wms.core.data.repository.api

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.model.InventorySummary

interface InventoryRepository {
    fun getInventoryOverviewStream(): Flow<InventorySummary?>
}
