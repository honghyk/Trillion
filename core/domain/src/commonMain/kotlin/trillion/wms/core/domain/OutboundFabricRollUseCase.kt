package trillion.wms.core.domain

import kotlinx.coroutines.flow.first
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.domain.utils.cancellableRunCatching
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundRequest

class OutboundFabricRollUseCase(
    private val zonesRepository: ZonesRepository,
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(request: OutboundRequest) = cancellableRunCatching {
        fabricRollsRepository.outboundFabricRoll(request)
        val updatedRoll = fabricRollsRepository.getFabricRoll(request.rollId).first()

        if (updatedRoll != null) {
            syncDependents(updatedRoll)
        }
    }

    private suspend fun syncDependents(fabricRoll: FabricRoll) {
        syncZone(fabricRoll.zoneId)
    }

    private suspend fun syncZone(zoneId: Long) {
        zonesRepository.refresh(zoneId)
    }
}
