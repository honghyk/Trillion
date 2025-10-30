package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.get
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.store.FabricRollStore
import trillion.wms.core.data.store.FabricRollsStore
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundRequest
import trillion.wms.core.model.UpdateFabricRollRequest
import trillion.wms.core.network.datasource.FabricRollRemoteDataSource

internal class DefaultFabricRollsRepository(
    private val local: FabricRollLocalDataSource,
    private val remote: FabricRollRemoteDataSource,
    private val fabricRollStore: FabricRollStore,
    private val fabricRollsStore: FabricRollsStore,
) : FabricRollsRepository {

    override fun getFabricRoll(id: Long): Flow<FabricRoll?> {
        return fabricRollStore
            .stream(StoreReadRequest.cached(id, refresh = false))
            .filter { it is StoreReadResponse.Data }
            .map { it.dataOrNull() }
    }

    override fun getFabricRolls(zoneId: Long, forceFresh: Boolean): Flow<List<FabricRoll>> {
        return fabricRollsStore
            .stream(StoreReadRequest.cached(zoneId, refresh = false))
            .filter { it is StoreReadResponse.Data }
            .map { it.requireData() }
    }

    override suspend fun addFabricRoll(request: AddFabricRollRequest): FabricRoll {
        val createdRoll = remote.addFabricRoll(request)
        local.upsert(createdRoll)

        return createdRoll
    }

    override suspend fun updateFabricRoll(request: UpdateFabricRollRequest): FabricRoll {
        val updatedRoll = remote.updateFabricRoll(request)
        local.upsert(updatedRoll)

        return updatedRoll
    }

    override suspend fun outboundFabricRoll(request: OutboundRequest) {
        val newHistory = remote.outboundFabricRoll(request)
        local.recordOutbound(newHistory)
    }

    override suspend fun deleteFabricRoll(id: Long) {
        remote.deleteFabricRoll(id)
        local.deleteById(id)
    }

    override suspend fun refresh(id: Long) {
        fabricRollStore.get(id)
    }

    override suspend fun refreshAllInZone(zoneId: Long) {
        fabricRollsStore.get(zoneId)
    }
}
