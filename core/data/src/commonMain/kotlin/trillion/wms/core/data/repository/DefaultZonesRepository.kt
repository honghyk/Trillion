package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.data.store.ZonesStore
import trillion.wms.core.data.store.ZoneStore
import trillion.wms.core.data.util.filterForResult
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
    private val zonesStore: ZonesStore,
) : ZonesRepository {

    override fun getZoneStream(id: Long): Flow<Zone?> {
        return zoneStore
            .stream(StoreReadRequest.cached(id, refresh = false))
            .filterForResult()
            .map { it.requireData() }
    }

    override fun getZoneByRollIdStream(rollId: Long): Flow<Zone?> {
        return fabricRollLocalDataSource.getFabricRollStream(rollId)
            .flatMapLatest { fabricRoll ->
                if (fabricRoll == null) {
                    flowOf(null)
                } else {
                    getZoneStream(fabricRoll.zoneId)
                }
            }
    }

    override fun getZonesStream(): Flow<List<Zone>> {
        return zonesStore
            .stream(StoreReadRequest.cached(Unit, refresh = false))
            .filterForResult()
            .map { it.requireData() }
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

    override suspend fun refresh(id: Long) {
        zoneStore.fresh(id)
    }

    override suspend fun refreshAll() {
        zonesStore.fresh(Unit)
    }
}
