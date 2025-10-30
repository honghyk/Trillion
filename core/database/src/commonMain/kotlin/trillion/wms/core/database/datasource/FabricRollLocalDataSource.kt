package trillion.wms.core.database.datasource

import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import trillion.wms.core.database.AppDatabase // Assuming AppDatabase is your RoomDatabase subclass
import trillion.wms.core.database.dao.FabricRollDao
import trillion.wms.core.database.dao.OutboundHistoryDao // Import OutboundHistoryDao
import trillion.wms.core.database.model.toDomain
import trillion.wms.core.database.model.toEntity
import trillion.wms.core.model.FabricRoll // Domain model
import trillion.wms.core.model.OutboundHistory // Domain model for history

interface FabricRollLocalDataSource {
    fun getFabricRollStream(id: Long): Flow<FabricRoll?>
    fun getAllFabricRollsStream(): Flow<List<FabricRoll>>
    fun getFabricRollsStreamByZoneId(zoneId: Long): Flow<List<FabricRoll>>
    fun searchFabricRolls(query: String, zoneId: Long?): Flow<List<FabricRoll>>
    suspend fun upsert(fabricRoll: FabricRoll)
    suspend fun upsertAll(fabricRolls: List<FabricRoll>)
    suspend fun delete(fabricRoll: FabricRoll)
    suspend fun deleteById(id: Long)
    suspend fun clearAll()

    suspend fun recordOutbound(history: OutboundHistory)
}

internal class RoomFabricRollLocalDataSource(
    private val appDatabase: AppDatabase,
    private val fabricRollDao: FabricRollDao,
    private val outboundHistoryDao: OutboundHistoryDao
) : FabricRollLocalDataSource {

    override fun getFabricRollStream(id: Long): Flow<FabricRoll?> {
        return fabricRollDao.getFabricRollStream(id).map { it?.toDomain() }
    }

    override fun getAllFabricRollsStream(): Flow<List<FabricRoll>> {
        return fabricRollDao.getAllFabricRollsStream().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFabricRollsStreamByZoneId(zoneId: Long): Flow<List<FabricRoll>> {
        return fabricRollDao.getFabricRollsStreamByZoneId(zoneId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchFabricRolls(query: String, zoneId: Long?): Flow<List<FabricRoll>> {
        return fabricRollDao.searchFabricRolls(query, zoneId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun upsert(fabricRoll: FabricRoll) {
        fabricRollDao.upsert(fabricRoll.toEntity())
    }

    override suspend fun upsertAll(fabricRolls: List<FabricRoll>) {
        fabricRollDao.upsertAll(fabricRolls.map { it.toEntity() })
    }

    override suspend fun delete(fabricRoll: FabricRoll) {
        fabricRollDao.delete(fabricRoll.toEntity())
    }

    override suspend fun deleteById(id: Long) {
        fabricRollDao.deleteById(id)
    }

    override suspend fun clearAll() {
        fabricRollDao.clearAll()
    }

    override suspend fun recordOutbound(history: OutboundHistory) {
        appDatabase.useWriterConnection {
            outboundHistoryDao.insert(history.toEntity())
            reduceRemainingQuantity(history)
        }
    }

    private suspend fun reduceRemainingQuantity(history: OutboundHistory) {
        val rollEntity = fabricRollDao.getFabricRollStream(history.rollId).first()
        if (rollEntity != null) {
            fabricRollDao.updateRemainingQuantity(
                rollEntity.id,
                rollEntity.remainingQuantity - history.quantity
            )
        }
    }
}
