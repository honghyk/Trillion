package trillion.wms.feature.fabricroll.detail.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.feature.fabricroll.detail.FabricRollDetailViewModel
import trillion.wms.core.domain.di.usecaseModule

val fabricRollDetailModule = module {
    includes(usecaseModule)

    viewModelOf(::FabricRollDetailViewModel)
}
