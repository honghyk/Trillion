package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.data.repository.api.RollOutboundHistoryRepository
import trillion.wms.core.model.OutboundHistory

class GetOutboundHistoriesUseCase(
    private val rollOutboundHistoryRepository: RollOutboundHistoryRepository,
) {

    operator fun invoke(
        rollId: Long,
        forceFresh: Boolean = false
    ): Flow<List<OutboundHistory>> {
        return rollOutboundHistoryRepository.getOutboundHistories(
            rollId = rollId,
            forceFresh = forceFresh
        )
    }
}
