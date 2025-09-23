package trillion.wms.core.network.api

internal enum class RemoteTable(val tableName: String) {
    ZONE("zones"),
    ZONE_WITH_STATS("v_zones_with_stats"),
    FABRIC_ROLL("fabric_rolls"),
    OUTBOUND_HISTORY("outbound_histories")
}
