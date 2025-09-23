package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.model.OutboundRequest

class OutboundFabricRollUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(request: OutboundRequest) {
        fabricRollsRepository.outboundFabricRoll(request)
    }
}
