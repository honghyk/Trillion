package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import trillion.wms.core.data.repository.api.SearchRepository
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.network.datasource.FabricRollRemoteDataSource

class DefaultSearchRepository(
    private val fabricRollLocalDataSource: FabricRollLocalDataSource,
    private val fabricRollRemoteDataSource: FabricRollRemoteDataSource,
) : SearchRepository {

    override fun searchFabricRolls(query: String, zoneId: Long?): Flow<List<FabricRoll>> {
        return if (query.isEmpty()) {
            flowOf(emptyList())
        } else {
            fabricRollLocalDataSource.searchFabricRolls(query = query, zoneId = zoneId)
                .onStart {
                    try {
                        val results = fabricRollRemoteDataSource.searchFabricRolls(query, zoneId)
                        fabricRollLocalDataSource.upsertAll(results)
                    } catch (_: Exception) {
                    }
                }
        }
    }
}
