package trillion.wms.di

import org.koin.core.context.startKoin
import org.koin.core.option.viewModelScopeFactory
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        modules(appModule)
        appDeclaration()
        options(
            viewModelScopeFactory()
        )
    }
}
