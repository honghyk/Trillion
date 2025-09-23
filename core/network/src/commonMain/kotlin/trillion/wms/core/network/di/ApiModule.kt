package trillion.wms.core.network.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import trillion.wms.core.network.BuildConfig
import trillion.wms.core.network.api.FabricRollApi
import trillion.wms.core.network.api.InventoryApi
import trillion.wms.core.network.api.OutboundHistoryApi
import trillion.wms.core.network.api.SupabaseFabricRollApi
import trillion.wms.core.network.api.SupabaseInventoryApi
import trillion.wms.core.network.api.SupabaseOutboundHistoryApi
import trillion.wms.core.network.api.SupabaseZoneApi
import trillion.wms.core.network.api.ZoneApi

val apiModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_API_KEY,
        ) {
            defaultLogLevel = LogLevel.DEBUG
            install(Postgrest)
        }
    }

    singleOf(::SupabaseZoneApi) bind ZoneApi::class
    singleOf(::SupabaseFabricRollApi) bind FabricRollApi::class
    singleOf(::SupabaseOutboundHistoryApi) bind OutboundHistoryApi::class
    singleOf(::SupabaseInventoryApi) bind InventoryApi::class
}
