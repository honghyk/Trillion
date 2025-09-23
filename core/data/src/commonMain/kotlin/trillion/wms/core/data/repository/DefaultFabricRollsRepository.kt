package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.InventoryRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.database.datasource.OutboundHistoryLocalDataSource
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.model.OutboundRequest
import trillion.wms.core.model.UpdateFabricRollRequest
import trillion.wms.core.network.datasource.FabricRollRemoteDataSource
import trillion.wms.core.network.datasource.OutboundHistoryRemoteDataSource

class DefaultFabricRollsRepository(
    private val zonesRepository: ZonesRepository,
    private val inventoryRepository: InventoryRepository,
    private val fabricRollLocalDataSource: FabricRollLocalDataSource,
    private val fabricRollRemoteDataSource: FabricRollRemoteDataSource,
    private val outboundHistoryLocalDataSource: OutboundHistoryLocalDataSource,
    private val outboundHistoryRemoteDataSource: OutboundHistoryRemoteDataSource,
) : FabricRollsRepository {

    override fun getFabricRollStream(id: Long, forceRefresh: Boolean): Flow<FabricRoll?> = flow {
        val localDataFlow = fabricRollLocalDataSource.getFabricRollStream(id)
        val currentLocalData = localDataFlow.first()

        if (forceRefresh || currentLocalData == null) {
            try {
                val remoteRoll = fabricRollRemoteDataSource.getFabricRollById(id)
                if (remoteRoll != null) {
                    fabricRollLocalDataSource.insert(remoteRoll)
                } else if (forceRefresh && currentLocalData != null) {
                    fabricRollLocalDataSource.deleteById(id)
                }
            } catch (e: Exception) {
                if (currentLocalData == null) throw e
            }
        }
        emitAll(localDataFlow)
    }

    override fun getFabricRollsStream(zoneId: Long, forceRefresh: Boolean): Flow<List<FabricRoll>> =
        flow {
            val localDataFlow = fabricRollLocalDataSource.getFabricRollsStreamByZoneId(zoneId)
            val currentLocalData = localDataFlow.first()

            if (forceRefresh || currentLocalData.isEmpty()) {
                try {
                    val remoteRolls = fabricRollRemoteDataSource.getFabricRolls(zoneId)
                    if (forceRefresh) {
                        fabricRollLocalDataSource.deleteByZoneId(zoneId)
                    }
                    fabricRollLocalDataSource.insertAll(remoteRolls)
                } catch (e: Exception) {
                    if (currentLocalData.isEmpty()) throw e
                }
            }
            emitAll(localDataFlow)
        }

    override fun getAllFabricRollsStream(forceRefresh: Boolean): Flow<List<FabricRoll>> = flow {
        val localDataFlow = fabricRollLocalDataSource.getAllFabricRollsStream()
        val currentLocalData = localDataFlow.first()

        if (forceRefresh || currentLocalData.isEmpty()) {
            try {
                val remoteRolls = fabricRollRemoteDataSource.getFabricRolls()
                if (forceRefresh) {
                    fabricRollLocalDataSource.clearAll()
                }
                fabricRollLocalDataSource.insertAll(remoteRolls)
            } catch (e: Exception) {
                if (currentLocalData.isEmpty()) throw e
            }
        }
        emitAll(localDataFlow)
    }

    override fun getOutboundHistoryStream(
        rollId: Long,
        forceRefresh: Boolean
    ): Flow<List<OutboundHistory>> = flow {
        val localDataFlow =
            outboundHistoryLocalDataSource.getOutboundHistoriesStreamByRollId(rollId)
        val currentLocalData = localDataFlow.first()

        if (forceRefresh || currentLocalData.isEmpty()) {
            try {
                val remoteHistory =
                    outboundHistoryRemoteDataSource.getOutboundHistoriesForRoll(rollId)
                if (forceRefresh) {
                    outboundHistoryLocalDataSource.deleteByRollId(rollId)
                }
                outboundHistoryLocalDataSource.insertAll(remoteHistory)
            } catch (e: Exception) {
                if (currentLocalData.isEmpty()) throw e
            }
        }
        emitAll(localDataFlow)
    }

    override suspend fun addFabricRoll(request: AddFabricRollRequest) {
        val createdRoll = fabricRollRemoteDataSource.addFabricRoll(request)
        fabricRollLocalDataSource.insert(createdRoll)

        zonesRepository.getZoneStream(request.zoneId, forceRefresh = true).first()
        inventoryRepository.getInventoryOverviewStream(true).first()
    }

    override suspend fun updateFabricRoll(request: UpdateFabricRollRequest) {
        val originalFabricRoll = fabricRollLocalDataSource.getFabricRollStream(request.id).first()
        val updatedRemoteRoll = fabricRollRemoteDataSource.updateFabricRoll(request)
        fabricRollLocalDataSource.update(updatedRemoteRoll)

        zonesRepository.getZoneStream(updatedRemoteRoll.zoneId, forceRefresh = true).first()
        if (originalFabricRoll != null && originalFabricRoll.zoneId != updatedRemoteRoll.zoneId) {
            zonesRepository.getZoneStream(originalFabricRoll.zoneId, forceRefresh = true).first()
        }
        inventoryRepository.getInventoryOverviewStream(true).first()
    }

    override suspend fun outboundFabricRoll(request: OutboundRequest) {
        val newHistory = fabricRollRemoteDataSource.outboundFabricRoll(request)
        val updatedRemoteRoll = fabricRollRemoteDataSource.getFabricRollById(request.rollId)

        if (updatedRemoteRoll != null) {
            fabricRollLocalDataSource.recordOutbound(newHistory, updatedRemoteRoll)
        } else {
            outboundHistoryLocalDataSource.insert(newHistory)
        }

        if (updatedRemoteRoll != null) {
            zonesRepository.getZoneStream(updatedRemoteRoll.zoneId, forceRefresh = true).first()
            inventoryRepository.getInventoryOverviewStream(true).first()
        }
    }

    override suspend fun deleteFabricRoll(id: Long) {
        val zoneId = fabricRollLocalDataSource.getFabricRollStream(id).first()?.zoneId

        fabricRollRemoteDataSource.deleteFabricRoll(id)
        fabricRollLocalDataSource.deleteById(id)

        if (zoneId != null) {
            zonesRepository.getZoneStream(zoneId, forceRefresh = true).first()
            inventoryRepository.getInventoryOverviewStream(true).first()
        }
    }

    override suspend fun deleteOutboundHistory(id: Long) {
        val localHistory = outboundHistoryLocalDataSource.getOutboundHistoryStream(id).first()
        outboundHistoryRemoteDataSource.deleteOutboundHistory(id)
        outboundHistoryLocalDataSource.deleteById(id)

        if (localHistory != null) {
            getFabricRollStream(localHistory.rollId, forceRefresh = true).first()
            val updatedRoll = fabricRollLocalDataSource.getFabricRollStream(localHistory.rollId).first()
            if (updatedRoll != null) {
                zonesRepository.getZoneStream(updatedRoll.zoneId, forceRefresh = true).first()
            }
            inventoryRepository.getInventoryOverviewStream(forceRefresh = true).first()
        }
    }
}
