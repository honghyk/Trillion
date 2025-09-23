package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.model.AddFabricRollRequest

class AddFabricRollUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(request: AddFabricRollRequest) {
        fabricRollsRepository.addFabricRoll(request)
    }
}
