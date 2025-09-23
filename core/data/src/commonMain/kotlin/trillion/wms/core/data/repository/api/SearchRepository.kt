package trillion.wms.core.data.repository.api

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.model.FabricRoll

interface SearchRepository {

    fun searchFabricRolls(query: String, zoneId: Long?): Flow<List<FabricRoll>>
}
