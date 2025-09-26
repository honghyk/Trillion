package trillion.wms.feature.zone.detail

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.core.domain.di.usecaseModule

val zoneDetailModule = module {
    includes(usecaseModule)

    viewModelOf(::ZoneDetailViewModel)
}
