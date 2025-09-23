package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import trillion.wms.core.data.repository.api.InventoryRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.database.datasource.ZoneLocalDataSource
import trillion.wms.core.model.CreateZoneRequest
import trillion.wms.core.model.UpdateZoneRequest
import trillion.wms.core.model.Zone
import trillion.wms.core.network.datasource.ZoneRemoteDataSource

class DefaultZonesRepository(
    private val inventoryRepository: InventoryRepository,
    private val zoneLocalDataSource: ZoneLocalDataSource,
    private val zoneRemoteDataSource: ZoneRemoteDataSource,
    private val fabricRollLocalDataSource: FabricRollLocalDataSource,
) : ZonesRepository {

    override fun getZoneStream(id: Long, forceRefresh: Boolean): Flow<Zone?> = flow {
        val localDataFlow = zoneLocalDataSource.getZoneStream(id)
        val currentLocalData = localDataFlow.first()

        if (forceRefresh || currentLocalData == null) {
            try {
                val remoteZone = zoneRemoteDataSource.getZone(id)
                if (remoteZone != null) {
                    zoneLocalDataSource.upsert(remoteZone)
                } else if (forceRefresh && currentLocalData != null) {
                    zoneLocalDataSource.deleteById(id)
                }
            } catch (e: Exception) {
                if (currentLocalData == null) throw e
            }
        }
        emitAll(localDataFlow)
    }

    override fun getZoneByRollIdStream(rollId: Long, forceRefresh: Boolean): Flow<Zone?> {
        return fabricRollLocalDataSource.getFabricRollStream(rollId)
            .flatMapLatest { fabricRoll ->
                if (fabricRoll == null) {
                    flowOf(null)
                } else {
                    getZoneStream(fabricRoll.zoneId, forceRefresh)
                }
            }
    }

    override fun getZonesStream(forceRefresh: Boolean): Flow<List<Zone>> = flow {
        val localDataFlow = zoneLocalDataSource.getAllZonesStream()
        val currentLocalData = localDataFlow.first()

        if (forceRefresh || currentLocalData.isEmpty()) {
            try {
                val remoteZones = zoneRemoteDataSource.getZones()
                if (forceRefresh) {
                    val remoteZoneIds = remoteZones.map { it.id }.toSet()
                    val localZonesToDelete = currentLocalData.filter { it.id !in remoteZoneIds }

                    for (zoneToDelete in localZonesToDelete) {
                        zoneLocalDataSource.deleteById(zoneToDelete.id)
                    }
                }
                zoneLocalDataSource.insertAll(remoteZones)
            } catch (e: Exception) {
                if (currentLocalData.isEmpty()) throw e
            }
        }
        emitAll(localDataFlow)
    }

    override suspend fun createZone(request: CreateZoneRequest) {
        val createdZone = zoneRemoteDataSource.createZone(request)
        zoneLocalDataSource.insert(createdZone)

        inventoryRepository.getInventoryOverviewStream(true).first()
    }

    override suspend fun updateZone(request: UpdateZoneRequest) {
        val updatedZone = zoneRemoteDataSource.updateZone(request)
        zoneLocalDataSource.update(updatedZone)

        inventoryRepository.getInventoryOverviewStream(true).first()
    }

    override suspend fun deleteZone(id: Long) {
        zoneRemoteDataSource.deleteZone(id)
        zoneLocalDataSource.deleteById(id)

        inventoryRepository.getInventoryOverviewStream(true).first()
    }
}
