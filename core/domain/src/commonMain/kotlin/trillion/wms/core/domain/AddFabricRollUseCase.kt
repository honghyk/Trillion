package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.domain.utils.cancellableRunCatching
import trillion.wms.core.model.AddFabricRollRequest

class AddFabricRollUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(request: AddFabricRollRequest) = cancellableRunCatching {
        fabricRollsRepository.addFabricRoll(request)
    }
}
