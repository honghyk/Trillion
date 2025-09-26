package trillion.wms.feature.fabricroll.form

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import trillion.wms.core.domain.di.usecaseModule

val fabricRollFormModule = module {
    includes(usecaseModule)

    viewModel { (zoneId: Long, rollId: Long?) ->
        FabricRollFormViewModel(
            zoneId = zoneId,
            rollId = rollId,
            get(),
            get(),
            get(),
            get(),
        )
    }
}
