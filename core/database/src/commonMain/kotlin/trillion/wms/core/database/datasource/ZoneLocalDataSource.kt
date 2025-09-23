package trillion.wms.core.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import trillion.wms.core.database.dao.ZoneDao
import trillion.wms.core.database.model.toDomain
import trillion.wms.core.database.model.toEntity
import trillion.wms.core.model.Zone // Domain model

interface ZoneLocalDataSource {
    fun getZoneStream(id: Long): Flow<Zone?>
    fun getAllZonesStream(): Flow<List<Zone>>
    suspend fun getZoneByName(name: String): Zone?
    suspend fun upsert(zone: Zone)
    suspend fun insert(zone: Zone): Long
    suspend fun insertAll(zones: List<Zone>)
    suspend fun update(zone: Zone)
    suspend fun delete(zone: Zone)
    suspend fun deleteById(id: Long)
    suspend fun clearAll()
}

internal class RoomZoneLocalDataSource(
    private val zoneDao: ZoneDao
) : ZoneLocalDataSource {

    override fun getZoneStream(id: Long): Flow<Zone?> {
        return zoneDao.getZoneStream(id).map { it?.toDomain() }
    }

    override fun getAllZonesStream(): Flow<List<Zone>> {
        return zoneDao.getAllZonesStream().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getZoneByName(name: String): Zone? {
        return zoneDao.getZoneByName(name)?.toDomain()
    }

    override suspend fun upsert(zone: Zone) {
        return zoneDao.upsert(zone.toEntity())
    }

    override suspend fun insert(zone: Zone): Long {
        return zoneDao.insert(zone.toEntity())
    }

    override suspend fun insertAll(zones: List<Zone>) {
        zoneDao.insertAll(zones.map { it.toEntity() })
    }

    override suspend fun update(zone: Zone) {
        zoneDao.update(zone.toEntity())
    }

    override suspend fun delete(zone: Zone) {
        zoneDao.delete(zone.toEntity())
    }

    override suspend fun deleteById(id: Long) {
        zoneDao.deleteById(id)
    }

    override suspend fun clearAll() {
        zoneDao.clearAll()
    }
}
