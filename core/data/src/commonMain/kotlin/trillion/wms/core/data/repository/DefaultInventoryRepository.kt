package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import trillion.wms.core.data.repository.api.InventoryRepository
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.database.datasource.ZoneLocalDataSource
import trillion.wms.core.model.InventorySummary

class DefaultInventoryRepository(
    private val fabricRollLocalDataSource: FabricRollLocalDataSource,
    private val zonesLocalDataSource: ZoneLocalDataSource,
) : InventoryRepository {

    override fun getInventoryOverviewStream(): Flow<InventorySummary?> {
        return combine(
            fabricRollLocalDataSource.getAllFabricRollsStream(),
            zonesLocalDataSource.getAllZonesStream(),
        ) { fabricRolls, zones ->
            InventorySummary(
                totalRollCount = fabricRolls.size,
                totalQuantity = fabricRolls.sumOf { it.remainingQuantity },
                totalZoneCount = zones.size,
            )
        }
    }
}
