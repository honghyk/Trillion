package trillion.wms.core.domain

import trillion.wms.core.data.repository.api.FabricRollsRepository

class RefreshFabricRollUseCase(
    private val fabricRollsRepository: FabricRollsRepository,
): Interactor<RefreshFabricRollUseCase.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        fabricRollsRepository.refresh(params.id)
    }

    data class Params(val id: Long, override val isUserInitiated: Boolean): UserInitiatedParams
}
