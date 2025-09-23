package trillion.wms.core.network.model

import kotlinx.serialization.Serializable
import trillion.wms.core.model.CreateZoneRequest

@Serializable
data class ZoneInsertPayload(
    val name: String,
    val description: String? = null
)

fun CreateZoneRequest.toInsertPayload(): ZoneInsertPayload = ZoneInsertPayload(
    name = this.name,
    description = this.description?.ifEmpty { null }
)
