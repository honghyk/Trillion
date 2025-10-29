package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.data.repository.api.InventoryRepository
import trillion.wms.core.model.InventorySummary

class GetInventoryOverviewStreamUseCase(
    private val inventoryRepository: InventoryRepository,
) {

    operator fun invoke(): Flow<InventorySummary?> {
        return inventoryRepository.getInventoryOverviewStream()
    }
}
