package trillion.wms.core.model

data class UpdateZoneRequest(
    val id: Long,
    val name: String?,
    val description: String?,
)
