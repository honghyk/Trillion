package trillion.wms.feature.fabricroll.detail

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.core.domain.di.usecaseModule

val fabricRollDetailModule = module {
    includes(usecaseModule)

    viewModelOf(::FabricRollDetailViewModel)
}
