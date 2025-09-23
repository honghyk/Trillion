package trillion.wms.core.network.datasource

import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import trillion.wms.core.model.CreateZoneRequest
import trillion.wms.core.model.UpdateZoneRequest
import trillion.wms.core.model.Zone
import trillion.wms.core.model.exception.AlreadyExistsException
import trillion.wms.core.network.api.ZoneApi
import trillion.wms.core.network.model.toDomain // For mapping DTO responses to Domain models
import trillion.wms.core.network.model.toInsertPayload // For mapping CreateZoneRequest to ZoneInsertPayload
import trillion.wms.core.network.model.toUpdatePayload // For mapping UpdateZoneRequest to ZoneUpdatePayload

interface ZoneRemoteDataSource {
    suspend fun getZone(zoneId: Long): Zone?
    suspend fun getZones(): List<Zone>
    suspend fun createZone(request: CreateZoneRequest): Zone
    suspend fun updateZone(request: UpdateZoneRequest): Zone
    suspend fun deleteZone(zoneId: Long)
}

internal class DefaultZoneRemoteDataSource(
    private val zoneApi: ZoneApi
) : ZoneRemoteDataSource {

    override suspend fun getZone(zoneId: Long): Zone? {
        return zoneApi.getZone(zoneId)?.toDomain()
    }

    override suspend fun getZones(): List<Zone> {
        return zoneApi.getZones().toDomain()
    }

    override suspend fun createZone(request: CreateZoneRequest): Zone {
        val payload = request.toInsertPayload()
        return try {
            zoneApi.createZone(payload).toDomain()
        } catch (e: PostgrestRestException) {
            when (e.statusCode) {
                409 -> throw AlreadyExistsException()
                else -> throw e
            }
        }
    }

    override suspend fun updateZone(request: UpdateZoneRequest): Zone {
        val payload = request.toUpdatePayload()
        return zoneApi.updateZone(request.id, payload).toDomain()
    }

    override suspend fun deleteZone(zoneId: Long) {
        zoneApi.deleteZone(zoneId)
    }
}
