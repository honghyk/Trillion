package trillion.wms.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import trillion.wms.core.model.InventorySummary

@Serializable
internal data class InventorySummaryDto(
    @SerialName("total_roll_count") val totalRollCount: Int,
    @SerialName("total_quantity") val totalQuantity: Double,
    @SerialName("total_zone_count") val totalZoneCount: Int,
)

internal fun InventorySummaryDto.toDomain() = InventorySummary(
    totalRollCount = totalRollCount,
    totalQuantity = totalQuantity,
    totalZoneCount = totalZoneCount,
)
