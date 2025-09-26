package trillion.wms.feature.inventory

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import trillion.wms.core.domain.di.usecaseModule

val inventoryModule = module {
    includes(usecaseModule)

    viewModelOf(::InventoryViewModel)
}
