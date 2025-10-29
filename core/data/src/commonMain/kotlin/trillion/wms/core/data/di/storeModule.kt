package trillion.wms.core.data.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import trillion.wms.core.data.repository.store.AllZonesStore
import trillion.wms.core.data.repository.store.ZoneStore
import trillion.wms.core.database.di.localDataSourceModule
import trillion.wms.core.network.di.remoteDataSourceModule

internal val storeModule = module {
    includes(localDataSourceModule, remoteDataSourceModule)

    factoryOf(::ZoneStore)
    factoryOf(::AllZonesStore)
}
