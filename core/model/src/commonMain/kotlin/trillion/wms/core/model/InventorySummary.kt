package trillion.wms.core.model

data class InventorySummary(
    val totalRollCount: Int,
    val totalQuantity: Double,
    val totalZoneCount: Int,
) {
    companion object Companion {
        val EMPTY = InventorySummary(
            totalRollCount = 0,
            totalQuantity = 0.0,
            totalZoneCount = 0,
        )
    }
}
