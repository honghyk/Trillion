package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.domain.utils.cancellableRunCatching

class DeleteOutboundHistoryUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(outboundHistoryId: Long) = cancellableRunCatching {
        fabricRollsRepository.deleteOutboundHistory(outboundHistoryId)
    }
}
