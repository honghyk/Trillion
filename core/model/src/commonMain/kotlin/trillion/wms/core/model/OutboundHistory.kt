package trillion.wms.core.model

import kotlin.time.Instant

data class OutboundHistory(
    val id: Long,
    val rollId: Long,
    val quantity: Double,
    val buyer: String,
    val remark: String,
    val createdAt: Instant,
)
