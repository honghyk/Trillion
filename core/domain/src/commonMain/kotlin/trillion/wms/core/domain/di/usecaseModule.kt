package trillion.wms.core.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import trillion.wms.core.data.di.repositoryModule
import trillion.wms.core.domain.*

val usecaseModule = module {
    includes(repositoryModule)

    factoryOf(::GetZonesStreamUseCase)
    factoryOf(::DeleteZoneUseCase)
    factoryOf(::CreateZoneUseCase)
    factoryOf(::GetZoneStreamUseCase)
    factoryOf(::GetFabricRollsStreamUseCase)
    factoryOf(::GetFabricRollStreamUseCase)
    factoryOf(::OutboundFabricRollUseCase)
    factoryOf(::AddFabricRollUseCase)
    factoryOf(::DeleteFabricRollUseCase)
    factoryOf(::GetOutboundHistoriesUseCase)
    factoryOf(::DeleteOutboundHistoryUseCase)
    factoryOf(::SearchFabricRollsStreamUseCase)
    factoryOf(::GetInventoryOverviewStreamUseCase)
    factoryOf(::UpdateFabricRollUseCase)
}
