package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.model.FabricRoll

class GetFabricRollStreamUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) {

    operator fun invoke(id: Long): Flow<FabricRoll?> {
        return fabricRollsRepository.getFabricRoll(id)
    }
}
