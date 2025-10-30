package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.domain.utils.cancellableRunCatching
import trillion.wms.core.model.CreateZoneRequest

class CreateZoneUseCase(
    private val zoneRepository: ZonesRepository
): Interactor<CreateZoneUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        zoneRepository.createZone(params.request)
    }

    data class Params(val request: CreateZoneRequest)
}
