package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.model.OutboundHistory

class GetOutboundHistoryStreamUseCase(
    private val fabricRollsRepository: FabricRollsRepository
) {

    operator fun invoke(
        fabricRollId: Long,
        forceRefresh: Boolean = false
    ): Flow<List<OutboundHistory>> {
        return fabricRollsRepository.getOutboundHistoryStream(fabricRollId, forceRefresh)
    }
}
