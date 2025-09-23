package trillion.wms.feature.zone.form.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.feature.zone.form.ZoneFormViewModel
import trillion.wms.core.domain.di.usecaseModule

val zoneFormModule = module {
    includes(usecaseModule)

    viewModelOf(::ZoneFormViewModel)
}
