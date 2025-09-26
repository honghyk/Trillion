package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.domain.utils.cancellableRunCatching
import trillion.wms.core.model.UpdateFabricRollRequest

class UpdateFabricRollUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(request: UpdateFabricRollRequest) = cancellableRunCatching {
        fabricRollsRepository.updateFabricRoll(request)
    }
}
