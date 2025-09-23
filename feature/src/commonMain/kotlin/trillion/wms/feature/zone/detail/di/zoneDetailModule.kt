package trillion.wms.feature.zone.detail.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.feature.zone.detail.ZoneDetailViewModel
import trillion.wms.core.domain.di.usecaseModule

val zoneDetailModule = module {
    includes(usecaseModule)

    viewModelOf(::ZoneDetailViewModel)
}
