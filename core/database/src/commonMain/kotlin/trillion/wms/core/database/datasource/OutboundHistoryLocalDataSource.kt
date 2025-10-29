package trillion.wms.core.database.datasource

import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import trillion.wms.core.database.AppDatabase
import trillion.wms.core.database.dao.FabricRollDao
import trillion.wms.core.database.dao.OutboundHistoryDao
import trillion.wms.core.database.model.OutboundHistoryEntity
import trillion.wms.core.database.model.toDomain // Assuming this extension function is in OutboundHistoryEntity.kt
import trillion.wms.core.database.model.toEntity // Assuming this extension function is in OutboundHistoryEntity.kt
import trillion.wms.core.model.OutboundHistory // Domain model

interface OutboundHistoryLocalDataSource {
    fun getOutboundHistories(rollId: Long): Flow<List<OutboundHistory>>
    suspend fun insert(outboundHistory: OutboundHistory): Long
    suspend fun insertAll(outboundHistories: List<OutboundHistory>)
    suspend fun delete(id: Long)
    suspend fun deleteByRollId(rollId: Long)
}

internal class RoomOutboundHistoryLocalDataSource(
    private val database: AppDatabase,
    private val outboundHistoryDao: OutboundHistoryDao,
    private val rollDao: FabricRollDao,
) : OutboundHistoryLocalDataSource {

    override fun getOutboundHistories(rollId: Long): Flow<List<OutboundHistory>> {
        return outboundHistoryDao.getOutboundHistoriesStreamByRollId(rollId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insert(outboundHistory: OutboundHistory): Long {
        return outboundHistoryDao.insert(outboundHistory.toEntity())
    }

    override suspend fun insertAll(outboundHistories: List<OutboundHistory>) {
        outboundHistoryDao.insertAll(outboundHistories.map { it.toEntity() })
    }

    override suspend fun delete(id: Long) {
        database.useWriterConnection {
            val historyEntity = outboundHistoryDao.getOutboundHistoryStream(id).first()
            outboundHistoryDao.delete(id)

            revertFabricRollQuantity(historyEntity)
        }
    }

    override suspend fun deleteByRollId(rollId: Long) {
        outboundHistoryDao.deleteByRollId(rollId)
    }

    private suspend fun revertFabricRollQuantity(history: OutboundHistoryEntity?) {
        history ?: return

        val fabricRoll = rollDao.getFabricRollStream(history.rollId).first()
        if (fabricRoll != null) {
            rollDao.updateRemainingQuantity(
                id = fabricRoll.id,
                remainingQuantity = fabricRoll.remainingQuantity + history.quantity
            )
        }
    }
}
