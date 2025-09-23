package trillion.wms.core.network.model

import kotlinx.serialization.Serializable
import trillion.wms.core.model.UpdateZoneRequest

@Serializable
data class ZoneUpdatePayload(
    val name: String? = null,
    val description: String? = null
)

fun UpdateZoneRequest.toUpdatePayload() = ZoneUpdatePayload(
    name = this.name,
    description = this.description
)
