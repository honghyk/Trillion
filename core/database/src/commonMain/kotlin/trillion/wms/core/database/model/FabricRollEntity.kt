package trillion.wms.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import trillion.wms.core.model.FabricRoll
import kotlin.time.Instant

@Entity(
    tableName = "fabric_rolls",
    foreignKeys = [
        ForeignKey(
            entity = ZoneEntity::class,
            parentColumns = ["id"],
            childColumns = ["zone_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("zone_id")
    ]
)
data class FabricRollEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo("zone_id") val zoneId: Long,
    @ColumnInfo("item_no") val itemNo: String?,
    @ColumnInfo("order_no") val orderNo: String?,
    @ColumnInfo("color") val color: String?,
    @ColumnInfo("factory") val factory: String?,
    @ColumnInfo("finish") val finish: String?,
    @ColumnInfo("remaining_quantity") val remainingQuantity: Double,
    @ColumnInfo("quantity") val quantity: Double,
    @ColumnInfo("remark") val remark: String?,
    @ColumnInfo("created_at") val createdAt: Instant,
)

fun FabricRollEntity.toDomain() = FabricRoll(
    id = id,
    zoneId = zoneId,
    itemNo = itemNo.orEmpty(),
    orderNo = orderNo.orEmpty(),
    color = color.orEmpty(),
    factory = factory.orEmpty(),
    finish = finish.orEmpty(),
    remainingQuantity = remainingQuantity,
    quantity = quantity,
    remark = remark.orEmpty(),
    createdAt = createdAt
)

fun FabricRoll.toEntity() = FabricRollEntity(
    id = id,
    zoneId = zoneId,
    itemNo = itemNo,
    orderNo = orderNo,
    color = color,
    factory = factory,
    finish = finish,
    remainingQuantity = remainingQuantity,
    quantity = quantity,
    remark = remark,
    createdAt = createdAt
)
