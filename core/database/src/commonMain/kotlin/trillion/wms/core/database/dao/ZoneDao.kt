package trillion.wms.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import trillion.wms.core.database.model.ZoneEntity

@Dao
internal interface ZoneDao {
    @Query("SELECT * FROM zones WHERE id = :id")
    fun getZoneStream(id: Long): Flow<ZoneEntity?>

    @Query("SELECT * FROM zones ORDER BY name ASC")
    fun getAllZonesStream(): Flow<List<ZoneEntity>>

    @Query("SELECT * FROM zones WHERE name = :name LIMIT 1")
    suspend fun getZoneByName(name: String): ZoneEntity?

    @Upsert
    suspend fun upsert(zone: ZoneEntity)

    @Upsert
    suspend fun upsertAll(zones: List<ZoneEntity>)

    @Delete
    suspend fun delete(zone: ZoneEntity)

    @Query("DELETE FROM zones WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM zones")
    suspend fun clearAll()
}
