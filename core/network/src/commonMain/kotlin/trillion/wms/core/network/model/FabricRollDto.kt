package trillion.wms.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import trillion.wms.core.model.FabricRoll
import kotlin.time.Instant

@Serializable
data class FabricRollDto(
    val id: Long,
    @SerialName("zone_id") val zoneId: Long,
    @SerialName("item_no") val itemNo: String? = null,
    @SerialName("order_no") val orderNo: String? = null,
    val color: String? = null,
    val factory: String? = null,
    val finish: String? = null,
    val quantity: Double,
    @SerialName("remaining_quantity") val remainingQuantity: Double,
    val remark: String? = null,
    @SerialName("inbound_at") val inboundAt: String,
    @SerialName("updated_at") val updatedAt: String,
)

fun FabricRollDto.toDomain(): FabricRoll {
    return FabricRoll(
        id = this.id,
        zoneId = this.zoneId,
        itemNo = this.itemNo ?: "",
        orderNo = this.orderNo ?: "",
        color = this.color ?: "",
        factory = this.factory ?: "",
        finish = this.finish ?: "",
        quantity = this.quantity,
        remainingQuantity = this.remainingQuantity,
        remark = this.remark ?: "",
        inboundAt = Instant.parse(this.inboundAt)
    )
}

fun List<FabricRollDto>.toDomain(): List<FabricRoll> {
    return this.map { it.toDomain() }
}
