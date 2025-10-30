package trillion.wms.feature.zone.form

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import trillion.wms.core.domain.di.usecaseModule

val zoneFormModule = module {
    includes(usecaseModule)

    viewModel { (zoneId: Long?) ->
        ZoneFormViewModel(zoneId, get(), get(), get())
    }
}
