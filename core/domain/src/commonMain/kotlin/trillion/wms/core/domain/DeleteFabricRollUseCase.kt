package trillion.wms.core.domain

import kotlinx.coroutines.flow.first
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.domain.utils.cancellableRunCatching
import trillion.wms.core.model.FabricRoll

class DeleteFabricRollUseCase(
    private val zonesRepository: ZonesRepository,
    private val fabricRollsRepository: FabricRollsRepository
) {

    suspend operator fun invoke(rollId: Long) = cancellableRunCatching {
        val fabricRoll = fabricRollsRepository.getFabricRoll(rollId).first()
        fabricRollsRepository.deleteFabricRoll(rollId)

        if (fabricRoll != null) {
            syncDependents(fabricRoll)
        }
    }

    private suspend fun syncDependents(fabricRoll: FabricRoll) {
        syncZone(fabricRoll.zoneId)
    }

    private suspend fun syncZone(zoneId: Long) {
        zonesRepository.refresh(zoneId)
    }
}
