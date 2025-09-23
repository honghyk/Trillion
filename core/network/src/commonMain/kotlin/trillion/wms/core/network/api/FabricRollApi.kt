package trillion.wms.core.network.api

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import trillion.wms.core.network.model.FabricRollDto
import trillion.wms.core.network.model.FabricRollInsertPayload
import trillion.wms.core.network.model.FabricRollUpdatePayload // Added import
import trillion.wms.core.network.model.OutboundHistoryDto
import trillion.wms.core.network.model.OutboundRpcParams

internal interface FabricRollApi {
    suspend fun getFabricRolls(zoneId: Long? = null): List<FabricRollDto>
    suspend fun getFabricRollById(id: Long): FabricRollDto?

    suspend fun addFabricRoll(payload: FabricRollInsertPayload): FabricRollDto
    suspend fun updateFabricRoll(
        rollId: Long,
        payload: FabricRollUpdatePayload
    ): FabricRollDto

    suspend fun deleteFabricRoll(rollId: Long)

    suspend fun outboundFabricRoll(params: OutboundRpcParams): OutboundHistoryDto

    suspend fun searchFabricRolls(
        query: String,
        zoneId: Long?
    ): List<FabricRollDto>
}

internal class SupabaseFabricRollApi(
    private val supabaseClient: SupabaseClient
) : FabricRollApi {

    override suspend fun getFabricRolls(zoneId: Long?): List<FabricRollDto> {
        return supabaseClient.from(RemoteTable.FABRIC_ROLL.tableName)
            .select {
                if (zoneId != null) {
                    filter { eq("zone_id", zoneId) }
                }
                order("id", order = Order.ASCENDING)
            }
            .decodeList()
    }

    override suspend fun getFabricRollById(id: Long): FabricRollDto? {
        return supabaseClient.from(RemoteTable.FABRIC_ROLL.tableName)
            .select {
                filter { eq("id", id) }
            }
            .decodeSingleOrNull()
    }

    override suspend fun addFabricRoll(payload: FabricRollInsertPayload): FabricRollDto {
        return supabaseClient.from(RemoteTable.FABRIC_ROLL.tableName)
            .insert(payload) {
                select()
            }
            .decodeSingle()
    }

    override suspend fun updateFabricRoll(
        rollId: Long,
        payload: FabricRollUpdatePayload
    ): FabricRollDto {
        return supabaseClient.from(RemoteTable.FABRIC_ROLL.tableName)
            .update(payload) {
                select()
                filter { eq("id", rollId) }
            }
            .decodeSingle()
    }

    override suspend fun outboundFabricRoll(params: OutboundRpcParams): OutboundHistoryDto {
        return supabaseClient.postgrest
            .rpc("perform_outbound", params)
            .decodeSingle()
    }

    override suspend fun deleteFabricRoll(rollId: Long) {
        supabaseClient.from(RemoteTable.FABRIC_ROLL.tableName)
            .delete {
                filter { eq("id", rollId) }
            }
    }

    override suspend fun searchFabricRolls(query: String, zoneId: Long?): List<FabricRollDto> {
        val searchTerm = "%$query%"
        return supabaseClient.from(RemoteTable.FABRIC_ROLL.tableName)
            .select {
                if (zoneId != null) {
                    filter { eq("zone_id", zoneId) }
                }
                filter {
                    or {
                        like("item_no", searchTerm)
                        like("order_no", searchTerm)
                        like("color", searchTerm)
                        like("factory", searchTerm)
                        like("finish", searchTerm)
                    }
                }
                order("created_at", order = Order.DESCENDING)
            }
            .decodeList()
    }
}
