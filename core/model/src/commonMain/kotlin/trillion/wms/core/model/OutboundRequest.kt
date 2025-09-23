package trillion.wms.core.model

import kotlin.time.Instant

data class OutboundRequest(
    val rollId: Long,
    val qtyToProcess: Double,
    val buyer: String,
    val date: Instant,
    val remark: String?
)
