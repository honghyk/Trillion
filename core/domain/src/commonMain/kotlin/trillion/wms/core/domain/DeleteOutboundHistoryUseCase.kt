package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository

class DeleteOutboundHistoryUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) {

    suspend operator fun invoke(outboundHistoryId: Long) {
        fabricRollsRepository.deleteOutboundHistory(outboundHistoryId)
    }
}
