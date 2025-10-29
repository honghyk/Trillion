package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.data.repository.store.AllZonesStore
import trillion.wms.core.data.repository.store.ZoneStore
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.database.datasource.ZoneLocalDataSource
import trillion.wms.core.model.CreateZoneRequest
import trillion.wms.core.model.UpdateZoneRequest
import trillion.wms.core.model.Zone
import trillion.wms.core.network.datasource.ZoneRemoteDataSource

internal class DefaultZonesRepository(
    private val zoneLocalDataSource: ZoneLocalDataSource,
    private val zoneRemoteDataSource: ZoneRemoteDataSource,
    private val fabricRollLocalDataSource: FabricRollLocalDataSource,
    private val zoneStore: ZoneStore,
    private val allZonesStore: AllZonesStore,
) : ZonesRepository {

    override fun getZoneStream(id: Long, refresh: Boolean): Flow<Zone?> {
        return zoneStore
            .stream(StoreReadRequest.cached(id, refresh))
            .filter { it is StoreReadResponse.Data }
            .map { response -> response.dataOrNull() }
            .distinctUntilChanged()
    }

    override fun getZoneByRollIdStream(rollId: Long, refresh: Boolean): Flow<Zone?> {
        return fabricRollLocalDataSource.getFabricRollStream(rollId)
            .flatMapLatest { fabricRoll ->
                if (fabricRoll == null) {
                    flowOf(null)
                } else {
                    getZoneStream(fabricRoll.zoneId, refresh)
                }
            }
    }

    override fun getZonesStream(refresh: Boolean): Flow<List<Zone>> {
        return allZonesStore
            .stream(StoreReadRequest.cached(Unit, refresh))
            .filter { it is StoreReadResponse.Data }
            .map { response -> response.dataOrNull() ?: emptyList() }
            .distinctUntilChanged()
    }

    override suspend fun createZone(request: CreateZoneRequest) {
        val createdZone = zoneRemoteDataSource.createZone(request)
        zoneLocalDataSource.upsert(createdZone)
    }

    override suspend fun updateZone(request: UpdateZoneRequest) {
        val updatedZone = zoneRemoteDataSource.updateZone(request)
        zoneLocalDataSource.upsert(updatedZone)
    }

    override suspend fun deleteZone(id: Long) {
        zoneRemoteDataSource.deleteZone(id)
        zoneLocalDataSource.deleteById(id)
    }
}
