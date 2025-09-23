package trillion.wms.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import trillion.wms.core.model.UpdateFabricRollRequest

@Serializable
data class FabricRollUpdatePayload(
    @SerialName("zone_id") val zoneId: Long? = null,
    @SerialName("item_no") val itemNo: String? = null,
    @SerialName("order_no") val orderNo: String? = null,
    val color: String? = null,
    val factory: String? = null,
    val finish: String? = null,
    val remark: String? = null,
    val quantity: Double? = null,
)

fun UpdateFabricRollRequest.toUpdatePayload(): FabricRollUpdatePayload = FabricRollUpdatePayload(
    zoneId = zoneId,
    itemNo = itemNo,
    orderNo = orderNo,
    color = color,
    factory = factory,
    finish = finish,
    remark = remark,
    quantity = quantity,
)
