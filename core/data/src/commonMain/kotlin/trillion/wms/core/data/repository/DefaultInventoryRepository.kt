package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import trillion.wms.core.data.repository.api.InventoryRepository
import trillion.wms.core.database.datasource.InventoryLocalDataSource
import trillion.wms.core.model.InventorySummary
import trillion.wms.core.network.datasource.InventoryRemoteDataSource

class DefaultInventoryRepository(
    private val inventoryLocalDataSource: InventoryLocalDataSource,
    private val inventoryRemoteDataSource: InventoryRemoteDataSource,
) : InventoryRepository {

    override fun getInventoryOverviewStream(forceRefresh: Boolean): Flow<InventorySummary?> = flow {
        val localData = inventoryLocalDataSource.getInventorySummary().first()

        if (forceRefresh || localData == null) {
            try {
                val remoteSummary = inventoryRemoteDataSource.getInventorySummary()
                inventoryLocalDataSource.setInventorySummary(remoteSummary)
            } catch (e: Exception) {
                if (localData == null) throw e
            }
        }

        emitAll(inventoryLocalDataSource.getInventorySummary())
    }
}
