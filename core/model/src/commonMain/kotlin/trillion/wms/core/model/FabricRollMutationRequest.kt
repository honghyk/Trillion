package trillion.wms.core.model

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
) : FabricRollMutationRequest
