package trillion.wms.core.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import trillion.wms.core.data.repository.DefaultFabricRollsRepository
import trillion.wms.core.data.repository.DefaultInventoryRepository
import trillion.wms.core.data.repository.DefaultSearchRepository
import trillion.wms.core.data.repository.DefaultZonesRepository
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.InventoryRepository
import trillion.wms.core.data.repository.api.SearchRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.database.di.localDataSourceModule
import trillion.wms.core.network.di.remoteDataSourceModule

val repositoryModule = module {
    includes(localDataSourceModule, remoteDataSourceModule)

    singleOf(::DefaultZonesRepository) bind ZonesRepository::class
    singleOf(::DefaultFabricRollsRepository) bind FabricRollsRepository::class
    singleOf(::DefaultSearchRepository) bind SearchRepository::class
    singleOf(::DefaultInventoryRepository) bind InventoryRepository::class
}
