package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.model.CreateZoneRequest

class CreateZoneUseCase(
    private val zoneRepository: ZonesRepository
) {

    suspend operator fun invoke(request: CreateZoneRequest) {
        zoneRepository.createZone(request)
    }
}
