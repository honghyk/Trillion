package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.ZonesRepository

class RefreshZoneUseCase(
    private val zonesRepository: ZonesRepository,
) : Interactor<RefreshZoneUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        zonesRepository.refresh(params.id)
    }

    data class Params(val id: Long, override val isUserInitiated: Boolean) : UserInitiatedParams
}
