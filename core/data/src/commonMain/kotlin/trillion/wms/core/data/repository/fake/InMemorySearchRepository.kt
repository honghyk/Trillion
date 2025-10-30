package trillion.wms.core.data.repository.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.SearchRepository
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.model.FabricRoll

class InMemorySearchRepository(
    private val fabricRollsRepository: FabricRollsRepository,
    private val fabricRollsLocalDataSource: FabricRollLocalDataSource,
) : SearchRepository {

    override fun searchFabricRolls(query: String, zoneId: Long?): Flow<List<FabricRoll>> {
        return if (zoneId == null) {
            fabricRollsLocalDataSource.getAllFabricRollsStream()
        } else {
            fabricRollsRepository.getFabricRolls(zoneId)
        }.map { fabricRolls ->
            if (query.isBlank()) {
                fabricRolls
            } else {
                fabricRolls.filter { roll ->
                    roll.id.toString().contains(query, ignoreCase = true)
                            || roll.itemNo.contains(query, ignoreCase = true)
                            || roll.orderNo?.contains(query, ignoreCase = true) == true
                            || roll.color?.contains(query, ignoreCase = true) == true
                            || roll.factory?.contains(query, ignoreCase = true) == true
                            || roll.finish?.contains(query, ignoreCase = true) == true
                }
            }
        }
    }
}
