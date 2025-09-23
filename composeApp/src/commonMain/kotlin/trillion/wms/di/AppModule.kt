package trillion.wms.di

import org.koin.dsl.module
import trillion.wms.feature.fabricroll.detail.di.fabricRollDetailModule
import trillion.wms.feature.fabricroll.form.di.fabricRollFormModule // Added import
import trillion.wms.feature.inventory.di.inventoryModule
import trillion.wms.feature.outbound.di.outboundFormModule
import trillion.wms.feature.zone.detail.di.zoneDetailModule
import trillion.wms.feature.zone.form.di.zoneFormModule
import trillion.wms.feature.zone.list.di.zonesModule

val appModule = module {
    includes(
        zonesModule,
        zoneFormModule,
        zoneDetailModule,
        outboundFormModule,
        fabricRollFormModule,
        fabricRollDetailModule,
        inventoryModule,
    )
}
