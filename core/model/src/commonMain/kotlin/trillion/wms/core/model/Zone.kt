package trillion.wms.core.model

import kotlin.time.Instant

data class Zone(
    val id: Long,
    val name: String,
    val description: String,
    val createdAt: Instant,
    val metrics: ZoneMetrics = ZoneMetrics()
) {
    companion object {
        val EMPTY = Zone(
            id = -1,
            name = "",
            description = "",
            createdAt = Instant.DISTANT_PAST,
        )
    }
}

data class ZoneMetrics(
    val rollCount: Int = 0,
    val totalQuantity: Double = 0.0,
)
