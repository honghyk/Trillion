package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.ZonesRepository

class RefreshFabricRollsUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
) : Interactor<RefreshFabricRollsUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        fabricRollsRepository.refreshAllInZone(params.zoneId)
    }

    data class Params(val zoneId: Long, override val isUserInitiated: Boolean) : UserInitiatedParams
}
