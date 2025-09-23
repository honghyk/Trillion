package trillion.wms.core.network.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import trillion.wms.core.network.datasource.DefaultFabricRollRemoteDataSource
import trillion.wms.core.network.datasource.DefaultInventoryRemoteDataSource
import trillion.wms.core.network.datasource.DefaultOutboundHistoryRemoteDataSource
import trillion.wms.core.network.datasource.DefaultZoneRemoteDataSource
import trillion.wms.core.network.datasource.FabricRollRemoteDataSource
import trillion.wms.core.network.datasource.InventoryRemoteDataSource
import trillion.wms.core.network.datasource.OutboundHistoryRemoteDataSource
import trillion.wms.core.network.datasource.ZoneRemoteDataSource

val remoteDataSourceModule = module {
    includes(apiModule)

    singleOf(::DefaultZoneRemoteDataSource) bind ZoneRemoteDataSource::class
    singleOf(::DefaultFabricRollRemoteDataSource) bind FabricRollRemoteDataSource::class
    singleOf(::DefaultOutboundHistoryRemoteDataSource) bind OutboundHistoryRemoteDataSource::class
    singleOf(::DefaultInventoryRemoteDataSource) bind InventoryRemoteDataSource::class
}
