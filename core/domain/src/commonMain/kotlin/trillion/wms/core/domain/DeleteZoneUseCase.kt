package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.ZonesRepository

class DeleteZoneUseCase(
    private val zonesRepository: ZonesRepository
) {

    suspend operator fun invoke(zoneId: Long) {
        zonesRepository.deleteZone(zoneId)
    }
}
