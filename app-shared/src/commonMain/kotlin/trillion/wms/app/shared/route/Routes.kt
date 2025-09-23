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
data class FabricRollForm(
    val zoneId: Long,
    val rollId: Long?
) : AppRoute

@Serializable
data object Inventory : AppRoute

@Serializable
data class OutboundForm(val rollId: Long) : AppRoute
