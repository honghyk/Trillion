package trillion.wms.core.network.api

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import trillion.wms.core.network.model.InventorySummaryDto

internal interface InventoryApi {
    suspend fun getInventorySummary(): InventorySummaryDto
}

internal class SupabaseInventoryApi(
    private val supabaseClient: SupabaseClient
) : InventoryApi {

    override suspend fun getInventorySummary(): InventorySummaryDto {
        return supabaseClient.postgrest
            .rpc("inventory_summary")
            .decodeSingle()
    }
}
