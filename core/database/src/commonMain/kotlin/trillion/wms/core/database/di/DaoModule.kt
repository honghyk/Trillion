package trillion.wms.core.database.di

import org.koin.dsl.module
import trillion.wms.core.database.AppDatabase

internal val daoModule = module {
    includes(databaseModule)

    single { get<AppDatabase>().zoneDao() }
    single { get<AppDatabase>().fabricRollDao() }
    single { get<AppDatabase>().outboundHistoryDao() }
}
