package trillion.wms.core.model

import kotlin.time.Instant

data class Zone(
    val id: Long,
    val name: String,
    val description: String,
    val createdAt: Instant,
    val stats: ZoneStats = ZoneStats()
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

data class ZoneStats(
    val rollCount: Int = 0,
    val totalQuantity: Double = 0.0,
)
