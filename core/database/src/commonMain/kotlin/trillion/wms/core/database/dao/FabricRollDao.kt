package trillion.wms.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import trillion.wms.core.database.model.FabricRollEntity

@Dao
internal interface FabricRollDao {
    @Query("SELECT * FROM fabric_rolls WHERE id = :id")
    fun getFabricRollStream(id: Long): Flow<FabricRollEntity?>

    @Query("SELECT * FROM fabric_rolls ORDER BY id ASC")
    fun getAllFabricRollsStream(): Flow<List<FabricRollEntity>>

    @Query("SELECT * FROM fabric_rolls WHERE zone_id = :zoneId ORDER BY id ASC")
    fun getFabricRollsStreamByZoneId(zoneId: Long): Flow<List<FabricRollEntity>>

    @Query(
        """
        SELECT * FROM fabric_rolls
        WHERE 
            (:zoneId IS NULL OR zone_id = :zoneId) AND 
            (
                CAST(id AS TEXT) LIKE '%' || :query || '%' OR 
                item_no LIKE '%' || :query || '%' OR 
                order_no LIKE '%' || :query || '%' OR 
                color LIKE '%' || :query || '%' OR 
                factory LIKE '%' || :query || '%' OR 
                finish LIKE '%' || :query || '%'
            )
        ORDER BY id ASC
    """
    )
    fun searchFabricRolls(query: String, zoneId: Long?): Flow<List<FabricRollEntity>>

    @Upsert
    suspend fun upsert(fabricRoll: FabricRollEntity): Long

    @Upsert
    suspend fun upsertAll(fabricRolls: List<FabricRollEntity>)

    @Query("UPDATE fabric_rolls SET remaining_quantity = :remainingQuantity WHERE id = :id")
    suspend fun updateRemainingQuantity(id: Long, remainingQuantity: Double)

    @Delete
    suspend fun delete(fabricRoll: FabricRollEntity)

    @Query("DELETE FROM fabric_rolls WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM fabric_rolls WHERE zone_id = :zoneId")
    suspend fun deleteByZoneId(zoneId: Long)

    @Query("DELETE FROM fabric_rolls")
    suspend fun clearAll()
}
