package trillion.wms.app.shared.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute

@Serializable
data object BaseZoneList : AppRoute

@Serializable
data object BaseInventory : AppRoute

@Serializable
data object ZoneList : AppRoute

@Serializable
data class ZoneDetail(val zoneId: Long) : AppRoute

@Serializable
data class ZoneForm(val zoneId: Long?) : AppRoute

@Serializable
data class FabricRollDetail(val rollId: Long) : AppRoute

@Serializable
class FabricRollForm private constructor(
    val zoneId: Long?,
    val rollId: Long?
) : AppRoute {
    companion object {
        fun add(zoneId: Long) = FabricRollForm(zoneId, null)
        fun edit(rollId: Long) = FabricRollForm(null, rollId)
    }
}

@Serializable
data object Inventory : AppRoute

@Serializable
data class OutboundForm(val rollId: Long) : AppRoute
