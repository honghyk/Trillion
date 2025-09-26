package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.domain.utils.cancellableRunCatching
import trillion.wms.core.model.OutboundRequest

class OutboundFabricRollUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(request: OutboundRequest) = cancellableRunCatching {
        fabricRollsRepository.outboundFabricRoll(request)
    }
}
