package trillion.wms.core.data.repository.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.InventoryRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.model.InventorySummary

class InMemoryInventoryRepository(
    private val zonesRepository: ZonesRepository,
    private val fabricRollsRepository: FabricRollsRepository,
) : InventoryRepository {

    override fun getInventoryOverviewStream(): Flow<InventorySummary> {
        return combine(
            zonesRepository.getZonesStream(),
            fabricRollsRepository.getAllFabricRolls(),
        ) { zones, fabricRolls ->
            InventorySummary(
                totalRollCount = fabricRolls.size,
                totalQuantity = fabricRolls.sumOf { it.quantity },
                totalZoneCount = zones.size,
            )
        }
    }
}
