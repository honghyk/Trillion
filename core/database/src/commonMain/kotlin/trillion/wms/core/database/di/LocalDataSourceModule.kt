package trillion.wms.core.database.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.database.datasource.InMemoryInventoryLocalDataSource
import trillion.wms.core.database.datasource.InventoryLocalDataSource
import trillion.wms.core.database.datasource.OutboundHistoryLocalDataSource
import trillion.wms.core.database.datasource.RoomFabricRollLocalDataSource
import trillion.wms.core.database.datasource.RoomOutboundHistoryLocalDataSource
import trillion.wms.core.database.datasource.RoomZoneLocalDataSource
import trillion.wms.core.database.datasource.ZoneLocalDataSource

val localDataSourceModule = module {
    includes(daoModule)

    singleOf(::RoomZoneLocalDataSource) bind ZoneLocalDataSource::class
    singleOf(::RoomFabricRollLocalDataSource) bind FabricRollLocalDataSource::class
    singleOf(::RoomOutboundHistoryLocalDataSource) bind OutboundHistoryLocalDataSource::class
    singleOf(::InMemoryInventoryLocalDataSource) bind InventoryLocalDataSource::class
}
