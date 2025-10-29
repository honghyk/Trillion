package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundRequest
import trillion.wms.core.model.UpdateFabricRollRequest
import trillion.wms.core.network.datasource.FabricRollRemoteDataSource

class DefaultFabricRollsRepository(
    private val local: FabricRollLocalDataSource,
    private val remote: FabricRollRemoteDataSource,
) : FabricRollsRepository {

    override fun getFabricRoll(
        id: Long,
        forceFresh: Boolean
    ): Flow<FabricRoll?> = networkBoundFlow(
        forceFresh = forceFresh,
        fetcher = { remote.getFabricRoll(id) },
        readEntity = local.getFabricRollStream(id),
        insertEntity = { local.insert(it) },
        deleteEntity = { local.deleteById(id) }
    )

    override fun getFabricRolls(zoneId: Long, forceFresh: Boolean): Flow<List<FabricRoll>> =
        networkBoundFlowExt(
            forceFresh = forceFresh,
            fetcher = { remote.getFabricRolls(zoneId) },
            readEntity = local.getFabricRollsStreamByZoneId(zoneId),
            insertEntity = { local.insertAll(it) },
            deleteEntity = { local.deleteByZoneId(zoneId) }
        )

    override fun getAllFabricRolls(forceFresh: Boolean): Flow<List<FabricRoll>> =
        networkBoundFlowExt(
            forceFresh = forceFresh,
            fetcher = { remote.getFabricRolls() },
            readEntity = local.getAllFabricRollsStream(),
            insertEntity = { local.insertAll(it) },
            deleteEntity = { local.clearAll() }
        )

    override suspend fun addFabricRoll(request: AddFabricRollRequest): FabricRoll {
        val createdRoll = remote.addFabricRoll(request)
        local.insert(createdRoll)

        return createdRoll
    }

    override suspend fun updateFabricRoll(request: UpdateFabricRollRequest): FabricRoll {
        val updatedRoll = remote.updateFabricRoll(request)
        local.update(updatedRoll)

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

    private fun <T : Any?> networkBoundFlow(
        forceFresh: Boolean,
        fetcher: suspend () -> T?,
        readEntity: Flow<T?>,
        insertEntity: suspend (T) -> Unit,
        deleteEntity: suspend () -> Unit,
    ): Flow<T?> = flow {
        val currentValue = readEntity.first()
        if (forceFresh || currentValue == null) {
            try {
                val remoteEntity = fetcher()
                if (remoteEntity == null) {
                    deleteEntity()
                } else {
                    insertEntity(remoteEntity)
                }
            } catch (e: Exception) {
                if (currentValue == null) throw e
            }
        }
        emitAll(readEntity)
    }

    private fun <T : Any> networkBoundFlowExt(
        forceFresh: Boolean,
        fetcher: suspend () -> List<T>,
        readEntity: Flow<List<T>>,
        insertEntity: suspend (List<T>) -> Unit,
        deleteEntity: suspend () -> Unit,
    ): Flow<List<T>> = flow {
        val currentValue = readEntity.first()
        if (forceFresh || currentValue.isEmpty()) {
            try {
                val remoteEntities = fetcher()
                if (forceFresh) {
                    deleteEntity()
                }
                insertEntity(remoteEntities)
            } catch (e: Exception) {
                if (currentValue.isEmpty()) throw e
            }
        }
        emitAll(readEntity)
    }
}
