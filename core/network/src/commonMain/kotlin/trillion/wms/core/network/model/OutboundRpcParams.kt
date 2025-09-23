package trillion.wms.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import trillion.wms.core.model.OutboundRequest
import kotlin.time.Instant

@Serializable
data class OutboundRpcParams(
    @SerialName("p_roll_id") val rollId: Long,
    @SerialName("p_qty_to_process") val qtyToProcess: Double,
    @SerialName("p_buyer") val buyer: String,
    @SerialName("p_outbound_date") val outboundDate: String,
    @SerialName("p_remark") val remark: String? = null
)

fun OutboundRequest.toRpcParams(): OutboundRpcParams {
    return OutboundRpcParams(
        rollId = this.rollId,
        qtyToProcess = this.qtyToProcess,
        buyer = this.buyer,
        outboundDate = this.date.toString(),
        remark = this.remark?.ifEmpty { null }
    )
}
