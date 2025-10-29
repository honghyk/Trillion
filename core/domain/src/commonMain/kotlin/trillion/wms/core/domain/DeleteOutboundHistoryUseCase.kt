package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.RollOutboundHistoryRepository
import trillion.wms.core.domain.utils.cancellableRunCatching

class DeleteOutboundHistoryUseCase(
    private val rollOutboundHistoryRepository: RollOutboundHistoryRepository,
) {

    suspend operator fun invoke(outboundHistoryId: Long) = cancellableRunCatching {
        rollOutboundHistoryRepository.deleteOutboundHistory(outboundHistoryId)
    }
}
