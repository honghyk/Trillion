package trillion.wms.feature.zone.list

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.core.domain.di.usecaseModule

val zonesModule = module {
    includes(usecaseModule)

    viewModelOf(::ZoneListViewModel)
}
