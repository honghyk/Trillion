package trillion.wms.feature.outbound

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.core.domain.di.usecaseModule

val outboundFormModule = module {
    includes(usecaseModule)
    viewModelOf(::OutboundFormViewModel)
}
