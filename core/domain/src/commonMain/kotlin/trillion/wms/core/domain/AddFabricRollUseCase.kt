package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.domain.utils.cancellableRunCatching
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll

class AddFabricRollUseCase(
    private val zonesRepository: ZonesRepository,
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(request: AddFabricRollRequest) = cancellableRunCatching {
        fabricRollsRepository.addFabricRoll(request).also {
            syncDependents(it)
        }
    }

    private suspend fun syncDependents(fabricRoll: FabricRoll) {
        syncZone(fabricRoll.zoneId)
    }

    private suspend fun syncZone(zoneId: Long) {
        zonesRepository.refresh(zoneId)
    }
}
