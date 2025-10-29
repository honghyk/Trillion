package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.ZonesRepository

class RefreshZonesUseCase(
    private val zonesRepository: ZonesRepository,
) : Interactor<RefreshZonesUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        zonesRepository.refreshAll()
    }

    data class Params(override val isUserInitiated: Boolean) : UserInitiatedParams
}
