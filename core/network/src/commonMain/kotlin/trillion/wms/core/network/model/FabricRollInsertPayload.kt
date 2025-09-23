package trillion.wms.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import trillion.wms.core.model.AddFabricRollRequest

@Serializable
data class FabricRollInsertPayload(
    val id: Long,
    @SerialName("zone_id") val zoneId: Long,
    @SerialName("item_no") val itemNo: String,
    @SerialName("order_no") val orderNo: String? = null,
    val color: String? = null,
    val factory: String? = null,
    val finish: String? = null,
    val quantity: Double,
    @SerialName("remaining_quantity") val remainingQuantity: Double,
    val remark: String? = null
)

fun AddFabricRollRequest.toInsertPayload(): FabricRollInsertPayload = FabricRollInsertPayload(
    id = id,
    zoneId = zoneId,
    itemNo = itemNo,
    orderNo = orderNo?.ifEmpty { null },
    color = color?.ifEmpty { null },
    factory = factory?.ifEmpty { null },
    finish = finish?.ifEmpty { null },
    quantity = quantity,
    remainingQuantity = quantity,
    remark = remark?.ifEmpty { null }
)
