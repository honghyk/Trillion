package trillion.wms.core.model

import kotlin.time.Instant

data class FabricRoll(
    val id: Long,
    val zoneId: Long,
    val itemNo: String,
    val orderNo: String,
    val color: String,
    val factory: String,
    val finish: String,
    val remark: String,
    val remainingQuantity: Double,
    val quantity: Double,
    val inboundAt: Instant
)

val FabricRoll.outboundQuantity: Double
    get() = quantity - remainingQuantity
