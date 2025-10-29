package trillion.wms.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import trillion.wms.core.model.Zone
import trillion.wms.core.model.ZoneMetrics // Import ZoneStats
import kotlin.time.Instant

@Serializable
data class ZoneDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("roll_count") val rollCount: Int? = null,
    @SerialName("total_quantity") val totalQuantity: Double? = null,
)

fun ZoneDto.toDomain(): Zone {
    return Zone(
        id = this.id,
        name = this.name,
        description = this.description.orEmpty(),
        createdAt = Instant.parse(this.createdAt),
        metrics = ZoneMetrics(
            rollCount = this.rollCount ?: 0,
            totalQuantity = this.totalQuantity ?: 0.0
        )
    )
}

fun List<ZoneDto>.toDomain(): List<Zone> {
    return this.map { it.toDomain() }
}
