package trillion.wms.core.data.repository.store

import org.mobilenativefoundation.store.store5.Converter
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import trillion.wms.core.database.datasource.ZoneLocalDataSource
import trillion.wms.core.model.Zone
import trillion.wms.core.network.datasource.ZoneRemoteDataSource

internal class ZoneStore(
    private val remote: ZoneRemoteDataSource,
    private val local: ZoneLocalDataSource,
) : Store<Long, Zone> by StoreBuilder
    .from(
        fetcher = Fetcher.of { id: Long ->
            remote.getZone(id) ?: throw IllegalArgumentException("Zone with ID $id not found")
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { id -> local.getZoneStream(id) },
            writer = { _, zone -> local.upsert(zone) }
        ),
        converter = Converter.Builder<Zone, Zone, Zone>()
            .fromOutputToLocal { it }
            .fromNetworkToLocal { it }
            .build()
    )
    .build()
