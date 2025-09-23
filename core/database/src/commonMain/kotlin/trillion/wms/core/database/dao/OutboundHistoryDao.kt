package trillion.wms.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import trillion.wms.core.database.model.OutboundHistoryEntity

@Dao
internal interface OutboundHistoryDao {
    @Query("SELECT * FROM outbound_histories WHERE id = :id")
    fun getOutboundHistoryStream(id: Long): Flow<OutboundHistoryEntity?>

    @Query("SELECT * FROM outbound_histories WHERE roll_id = :rollId ORDER BY created_at DESC")
    fun getOutboundHistoriesStreamByRollId(rollId: Long): Flow<List<OutboundHistoryEntity>>

    @Query("SELECT * FROM outbound_histories ORDER BY created_at DESC")
    fun getAllOutboundHistoriesStream(): Flow<List<OutboundHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(outboundHistory: OutboundHistoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(outboundHistories: List<OutboundHistoryEntity>)

    @Delete
    suspend fun delete(outboundHistory: OutboundHistoryEntity)

    @Query("DELETE FROM outbound_histories WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM outbound_histories WHERE roll_id = :rollId")
    suspend fun deleteByRollId(rollId: Long)

    @Query("DELETE FROM outbound_histories")
    suspend fun clearAll()
}
