package trillion.wms.core.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import trillion.wms.core.database.dao.OutboundHistoryDao
import trillion.wms.core.database.model.toDomain // Assuming this extension function is in OutboundHistoryEntity.kt
import trillion.wms.core.database.model.toEntity // Assuming this extension function is in OutboundHistoryEntity.kt
import trillion.wms.core.model.OutboundHistory // Domain model

interface OutboundHistoryLocalDataSource {
    fun getOutboundHistoryStream(id: Long): Flow<OutboundHistory?>
    fun getOutboundHistoriesStreamByRollId(rollId: Long): Flow<List<OutboundHistory>>
    fun getAllOutboundHistoriesStream(): Flow<List<OutboundHistory>>
    suspend fun insert(outboundHistory: OutboundHistory): Long
    suspend fun insertAll(outboundHistories: List<OutboundHistory>)
    suspend fun delete(outboundHistory: OutboundHistory)
    suspend fun deleteById(id: Long)
    suspend fun deleteByRollId(rollId: Long)
    suspend fun clearAll()
}

internal class RoomOutboundHistoryLocalDataSource(
    private val outboundHistoryDao: OutboundHistoryDao
) : OutboundHistoryLocalDataSource {

    override fun getOutboundHistoryStream(id: Long): Flow<OutboundHistory?> {
        return outboundHistoryDao.getOutboundHistoryStream(id).map { entity -> entity?.toDomain() }
    }

    override fun getOutboundHistoriesStreamByRollId(rollId: Long): Flow<List<OutboundHistory>> {
        return outboundHistoryDao.getOutboundHistoriesStreamByRollId(rollId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllOutboundHistoriesStream(): Flow<List<OutboundHistory>> {
        return outboundHistoryDao.getAllOutboundHistoriesStream().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insert(outboundHistory: OutboundHistory): Long {
        return outboundHistoryDao.insert(outboundHistory.toEntity())
    }

    override suspend fun insertAll(outboundHistories: List<OutboundHistory>) {
        outboundHistoryDao.insertAll(outboundHistories.map { it.toEntity() })
    }

    override suspend fun delete(outboundHistory: OutboundHistory) {
        outboundHistoryDao.delete(outboundHistory.toEntity())
    }

    override suspend fun deleteById(id: Long) {
        outboundHistoryDao.deleteById(id)
    }

    override suspend fun deleteByRollId(rollId: Long) {
        outboundHistoryDao.deleteByRollId(rollId)
    }

    override suspend fun clearAll() {
        outboundHistoryDao.clearAll()
    }
}
