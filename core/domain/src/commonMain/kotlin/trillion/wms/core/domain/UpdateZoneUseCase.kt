package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.model.UpdateZoneRequest

class UpdateZoneUseCase(
    private val zonesRepository: ZonesRepository,
) : Interactor<UpdateZoneUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        zonesRepository.updateZone(params.request)
    }

    data class Params(val request: UpdateZoneRequest)
}
