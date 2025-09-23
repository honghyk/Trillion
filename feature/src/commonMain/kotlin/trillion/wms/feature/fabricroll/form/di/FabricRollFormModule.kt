package trillion.wms.feature.fabricroll.form.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import trillion.wms.core.domain.di.usecaseModule
import trillion.wms.feature.fabricroll.form.FabricRollFormViewModel

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
