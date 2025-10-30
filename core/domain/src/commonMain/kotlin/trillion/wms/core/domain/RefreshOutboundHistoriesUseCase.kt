package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.RollOutboundHistoryRepository

class RefreshOutboundHistoriesUseCase(
    private val outboundHistoryRepository: RollOutboundHistoryRepository,
): Interactor<RefreshOutboundHistoriesUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        outboundHistoryRepository.refresh(params.rollId)
    }

    data class Params(val rollId: Long, override val isUserInitiated: Boolean): UserInitiatedParams
}
