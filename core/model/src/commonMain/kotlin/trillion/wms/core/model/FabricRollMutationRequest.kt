package trillion.wms.core.model

import kotlin.time.Instant

sealed interface FabricRollMutationRequest {
    val id: Long
}

data class AddFabricRollRequest(
    override val id: Long,
    val zoneId: Long,
    val itemNo: String,
    val orderNo: String?,
    val color: String?,
    val factory: String?,
    val finish: String?,
    val remark: String?,
    val quantity: Double,
    val inboundAt: Instant,
) : FabricRollMutationRequest

data class UpdateFabricRollRequest(
    override val id: Long,
    val zoneId: Long?,
    val itemNo: String?,
    val orderNo: String?,
    val color: String?,
    val factory: String?,
    val finish: String?,
    val remark: String?,
    val quantity: Double?,
    val inboundAt: Instant?,
) : FabricRollMutationRequest
