package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository

class DeleteFabricRollUseCase(
    private val fabricRollsRepository: FabricRollsRepository
) {

    suspend operator fun invoke(rollId: Long) {
        fabricRollsRepository.deleteFabricRoll(rollId)
    }
}
