package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.domain.utils.cancellableRunCatching

class DeleteZoneUseCase(
    private val zonesRepository: ZonesRepository
) {

    suspend operator fun invoke(zoneId: Long) = cancellableRunCatching {
        zonesRepository.deleteZone(zoneId)
    }
}
