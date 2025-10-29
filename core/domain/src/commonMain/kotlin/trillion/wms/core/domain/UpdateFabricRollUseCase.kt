package trillion.wms.core.domain

import kotlinx.coroutines.flow.first
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.domain.utils.cancellableRunCatching
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.UpdateFabricRollRequest

class UpdateFabricRollUseCase(
    private val zonesRepository: ZonesRepository,
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(request: UpdateFabricRollRequest) = cancellableRunCatching {
        val originalRoll = fabricRollsRepository.getFabricRoll(request.id).first()
        val updatedRoll = fabricRollsRepository.updateFabricRoll(request)

        syncDependents(originalRoll, updatedRoll)
    }

    private suspend fun syncDependents(
        originalRoll: FabricRoll?,
        updatedRoll: FabricRoll,
    ) {
        val zonesToSync = listOfNotNull(
            updatedRoll.zoneId,
            originalRoll?.zoneId?.takeIf { it != updatedRoll.zoneId }
        )
        syncZones(zonesToSync)
    }

    private suspend fun syncZones(zoneIds: List<Long>) {
        for (zoneId in zoneIds) {
            zonesRepository.refresh(zoneId)
        }
    }
}
