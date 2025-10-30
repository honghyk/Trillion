package trillion.wms.core.data.store

import kotlinx.coroutines.flow.first
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import trillion.wms.core.data.util.storeBuilder
import trillion.wms.core.database.datasource.OutboundHistoryLocalDataSource
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.network.datasource.OutboundHistoryRemoteDataSource

class OutboundHistoriesStore(
    private val local: OutboundHistoryLocalDataSource,
    private val remote: OutboundHistoryRemoteDataSource,
) : Store<Long, List<OutboundHistory>> by storeBuilder(
    fetcher = Fetcher.of { rollId: Long ->
        remote.getOutboundHistoriesForRoll(rollId)
    },
    sourceOfTruth = SourceOfTruth.of(
        reader = { rollId -> local.getOutboundHistories(rollId) },
        writer = { rollId, remoteValues ->
            val remoteIdSet = remoteValues.map { it.id }.toSet()
            val currentLocalEntities = local.getOutboundHistories(rollId).first()

            val deleted = currentLocalEntities.filter { it.id !in remoteIdSet }

            local.insertAll(remoteValues)
            deleted.forEach { local.delete(it.id) }
        }
    )
).build()
