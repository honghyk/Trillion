package trillion.wms.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import trillion.wms.core.model.OutboundHistory
import kotlin.time.Instant

@Entity(
    tableName = "outbound_histories",
    foreignKeys = [
        ForeignKey(
            entity = FabricRollEntity::class,
            parentColumns = ["id"],
            childColumns = ["roll_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("roll_id")
    ]
)
data class OutboundHistoryEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo("roll_id") val rollId: Long,
    @ColumnInfo("quantity") val quantity: Double,
    @ColumnInfo("created_at") val createdAt: Instant,
    @ColumnInfo("buyer") val buyer: String,
    @ColumnInfo("remark") val remark: String?,
)

fun OutboundHistoryEntity.toDomain() = OutboundHistory(
    id = id,
    rollId = rollId,
    quantity = quantity,
    createdAt = createdAt,
    buyer = buyer,
    remark = remark.orEmpty(),
)

fun OutboundHistory.toEntity() = OutboundHistoryEntity(
    id = id,
    rollId = rollId,
    quantity = quantity,
    createdAt = createdAt,
    buyer = buyer,
    remark = remark,
)
