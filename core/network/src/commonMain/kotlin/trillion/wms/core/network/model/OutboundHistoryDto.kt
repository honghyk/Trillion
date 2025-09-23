package trillion.wms.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import trillion.wms.core.model.OutboundHistory
import kotlin.time.Instant

@Serializable
data class OutboundHistoryDto(
    val id: Long,
    @SerialName("roll_id") val rollId: Long,
    val quantity: Double,
    val buyer: String,
    val remark: String? = null,
    @SerialName("created_at") val createdAt: String
)

fun OutboundHistoryDto.toDomain(): OutboundHistory {
    return OutboundHistory(
        id = this.id,
        rollId = this.rollId,
        quantity = this.quantity,
        buyer = this.buyer,
        remark = this.remark ?: "",
        createdAt = Instant.parse(this.createdAt)
    )
}

fun List<OutboundHistoryDto>.toDomain(): List<OutboundHistory> {
    return this.map { it.toDomain() }
}
