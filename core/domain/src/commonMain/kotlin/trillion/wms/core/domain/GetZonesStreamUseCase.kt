package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.model.Zone

class GetZonesStreamUseCase(
    private val zonesRepository: ZonesRepository
) {

    operator fun invoke(): Flow<List<Zone>> {
        return zonesRepository.getZonesStream()
    }
}
