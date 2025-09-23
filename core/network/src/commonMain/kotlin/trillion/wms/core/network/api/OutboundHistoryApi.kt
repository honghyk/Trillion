package trillion.wms.core.network.api

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import trillion.wms.core.network.model.OutboundHistoryDto

internal interface OutboundHistoryApi {
    suspend fun getOutboundHistoriesForRoll(rollId: Long): List<OutboundHistoryDto>
    suspend fun getAllOutboundHistories(): List<OutboundHistoryDto>
    suspend fun deleteOutboundHistory(id: Long): OutboundHistoryDto?
}

internal class SupabaseOutboundHistoryApi(
    private val supabaseClient: SupabaseClient
) : OutboundHistoryApi {

    override suspend fun getOutboundHistoriesForRoll(rollId: Long): List<OutboundHistoryDto> {
        return supabaseClient.from(RemoteTable.OUTBOUND_HISTORY.tableName)
            .select {
                filter {
                    eq("roll_id", rollId)
                }
                order("created_at", Order.DESCENDING)
            }
            .decodeList()
    }

    override suspend fun getAllOutboundHistories(): List<OutboundHistoryDto> {
        return supabaseClient.from(RemoteTable.OUTBOUND_HISTORY.tableName)
            .select {
                order("created_at", Order.DESCENDING)
            }
            .decodeList()
    }

    override suspend fun deleteOutboundHistory(id: Long): OutboundHistoryDto? {
        val response = supabaseClient.postgrest
            .rpc(
                function = "delete_outbound_history",
                parameters = mapOf("p_id" to id)
            )

        if (response.data == "null") {
            return null
        }
        return response.decodeAs()
    }
}
