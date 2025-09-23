package trillion.wms.feature.outbound.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.feature.outbound.OutboundFormViewModel
import trillion.wms.core.domain.di.usecaseModule

val outboundFormModule = module {
    includes(usecaseModule)
    viewModelOf(::OutboundFormViewModel)
}
