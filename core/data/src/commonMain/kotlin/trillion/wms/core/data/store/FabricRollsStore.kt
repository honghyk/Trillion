package trillion.wms.core.data.store

import kotlinx.coroutines.flow.first
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import trillion.wms.core.data.util.storeBuilder
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.network.datasource.FabricRollRemoteDataSource

class FabricRollsStore(
    private val remote: FabricRollRemoteDataSource,
    private val local: FabricRollLocalDataSource,
) : Store<Long, List<FabricRoll>> by storeBuilder(
    fetcher = Fetcher.of { zoneId: Long -> remote.getFabricRolls(zoneId) },
    sourceOfTruth = SourceOfTruth.of(
        reader = { zoneId -> local.getFabricRollsStreamByZoneId(zoneId) },
        writer = { zoneId, remoteValues ->
            val remoteIdSet = remoteValues.map { it.id }.toSet()
            val currentLocalEntities = local.getFabricRollsStreamByZoneId(zoneId).first()

            val deleted = currentLocalEntities.filter { it.id !in remoteIdSet }

            local.upsertAll(remoteValues)
            deleted.forEach { local.delete(it) }
        }
    )
).build()
