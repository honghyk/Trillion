package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.domain.utils.cancellableRunCatching

class DeleteFabricRollUseCase(
    private val fabricRollsRepository: FabricRollsRepository
) {

    suspend operator fun invoke(rollId: Long) = cancellableRunCatching {
        fabricRollsRepository.deleteFabricRoll(rollId)
    }
}
