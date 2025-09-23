package trillion.wms.core.domain

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.data.repository.api.SearchRepository
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.Zone

class SearchFabricRollsStreamUseCase(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(query: String, zoneId: Long?): Flow<List<FabricRoll>> {
        return searchRepository.searchFabricRolls(query, zoneId)
    }
}
