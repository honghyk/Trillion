package trillion.wms.core.network.datasource

import co.touchlab.kermit.Logger
import trillion.wms.core.model.InventorySummary
import trillion.wms.core.network.api.InventoryApi
import trillion.wms.core.network.model.toDomain

interface InventoryRemoteDataSource {
    suspend fun getInventorySummary(): InventorySummary
}

internal class DefaultInventoryRemoteDataSource(
    private val inventoryApi: InventoryApi,
) : InventoryRemoteDataSource {

    override suspend fun getInventorySummary(): InventorySummary {
        return try {
            inventoryApi.getInventorySummary().toDomain()
        } catch (e: Exception) {
            Logger.d { "$e" }
            throw e
        }
    }
}
