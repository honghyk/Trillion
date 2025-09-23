package trillion.wms.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import trillion.wms.core.model.Zone
import trillion.wms.core.model.ZoneStats // Import ZoneStats
import kotlin.time.Instant

@Entity(
    tableName = "zones",
    indices = [Index(value = ["name"], unique = true)]
)
data class ZoneEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    @ColumnInfo("created_at") val createdAt: Instant,
    @ColumnInfo("roll_count") val rollCount: Int = 0,
    @ColumnInfo("total_quantity") val totalQuantity: Double = 0.0,
)

fun ZoneEntity.toDomain() = Zone(
    id = id,
    name = name,
    description = description.orEmpty(),
    createdAt = createdAt,
    stats = ZoneStats(
        rollCount = rollCount,
        totalQuantity = totalQuantity
    )
)

fun Zone.toEntity() = ZoneEntity(
    id = id,
    name = name,
    description = description.ifEmpty { null },
    createdAt = createdAt,
    rollCount = stats.rollCount,
    totalQuantity = stats.totalQuantity
)
