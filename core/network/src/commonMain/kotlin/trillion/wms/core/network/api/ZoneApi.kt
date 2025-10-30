package trillion.wms.core.network.api

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import trillion.wms.core.network.model.ZoneDto
import trillion.wms.core.network.model.ZoneInsertPayload
import trillion.wms.core.network.model.ZoneUpdatePayload

internal interface ZoneApi {
    suspend fun getZone(zoneId: Long): ZoneDto?
    suspend fun getZones(): List<ZoneDto>
    suspend fun createZone(payload: ZoneInsertPayload): ZoneDto
    suspend fun updateZone(zoneId: Long, payload: ZoneUpdatePayload): ZoneDto
    suspend fun deleteZone(zoneId: Long)
}

internal class SupabaseZoneApi(
    private val supabaseClient: SupabaseClient
) : ZoneApi {

    override suspend fun getZone(zoneId: Long): ZoneDto? {
        return supabaseClient.from(RemoteTable.ZONE_WITH_STATS.tableName)
            .select {
                filter { eq("id", zoneId) }
            }
            .decodeSingleOrNull()
    }

    override suspend fun getZones(): List<ZoneDto> {
        return supabaseClient.from(RemoteTable.ZONE_WITH_STATS.tableName)
            .select()
            .decodeList()
    }

    override suspend fun createZone(payload: ZoneInsertPayload): ZoneDto {
        return supabaseClient.from(RemoteTable.ZONE.tableName)
            .insert(payload) {
                select()
            }
            .decodeSingle()
    }

    override suspend fun updateZone(zoneId: Long, payload: ZoneUpdatePayload): ZoneDto {
        return supabaseClient.from(RemoteTable.ZONE.tableName)
            .update(payload) {
                select()
                filter { eq("id", zoneId) }
            }
            .decodeSingle()
    }

    override suspend fun deleteZone(zoneId: Long) {
        supabaseClient.from(RemoteTable.ZONE.tableName)
            .delete {
                filter { eq("id", zoneId) }
            }
    }
}
