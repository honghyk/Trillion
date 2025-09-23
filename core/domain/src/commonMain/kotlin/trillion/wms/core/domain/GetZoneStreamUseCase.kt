package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.model.Zone

class GetZoneStreamUseCase(
    private val zonesRepository: ZonesRepository,
) {

    operator fun invoke(params: Params, forceRefresh: Boolean = false): Flow<Zone?> {
        return when (params) {
            is Params.ZoneId -> zonesRepository.getZoneStream(params.id, forceRefresh)
            is Params.RollId -> zonesRepository.getZoneByRollIdStream(params.id, forceRefresh)
        }
    }

    sealed interface Params {
        data class ZoneId(val id: Long) : Params
        data class RollId(val id: Long) : Params
    }
}
