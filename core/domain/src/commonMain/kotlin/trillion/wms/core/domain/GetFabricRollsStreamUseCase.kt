package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.model.FabricRoll

class GetFabricRollsStreamUseCase(
    private val fabricRollsRepository: FabricRollsRepository
) {

    operator fun invoke(zoneId: Long, forceRefresh: Boolean = false): Flow<List<FabricRoll>> {
        return fabricRollsRepository.getFabricRolls(zoneId, forceRefresh)
    }
}
