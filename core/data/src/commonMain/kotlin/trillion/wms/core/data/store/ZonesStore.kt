package trillion.wms.core.data.store

import kotlinx.coroutines.flow.first
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import trillion.wms.core.data.util.storeBuilder
import trillion.wms.core.database.datasource.ZoneLocalDataSource
import trillion.wms.core.model.Zone
import trillion.wms.core.network.datasource.ZoneRemoteDataSource

internal class ZonesStore(
    private val remote: ZoneRemoteDataSource,
    private val local: ZoneLocalDataSource,
) : Store<Unit, List<Zone>> by storeBuilder(
    fetcher = Fetcher.of { _: Unit -> remote.getZones() },
    sourceOfTruth = SourceOfTruth.of(
        reader = { _ -> local.getAllZonesStream() },
        writer = { _, remoteValues ->
            val remoteIdSet = remoteValues.map { it.id }.toSet()
            val currentLocalEntities = local.getAllZonesStream().first()

            val deleted = currentLocalEntities.filter { it.id !in remoteIdSet }

            local.upsertAll(remoteValues)
            deleted.forEach { local.delete(it) }
        }
    )
).build()
