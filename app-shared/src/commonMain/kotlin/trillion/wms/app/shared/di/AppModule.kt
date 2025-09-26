package trillion.wms.app.shared.di

import org.koin.dsl.module
import trillion.wms.feature.fabricroll.detail.fabricRollDetailModule
import trillion.wms.feature.fabricroll.form.fabricRollFormModule
import trillion.wms.feature.inventory.inventoryModule
import trillion.wms.feature.outbound.outboundFormModule
import trillion.wms.feature.zone.detail.zoneDetailModule
import trillion.wms.feature.zone.form.zoneFormModule
import trillion.wms.feature.zone.list.zonesModule

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
