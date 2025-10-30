package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import trillion.wms.core.data.repository.api.RollOutboundHistoryRepository
import trillion.wms.core.data.store.OutboundHistoriesStore
import trillion.wms.core.database.datasource.OutboundHistoryLocalDataSource
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.network.datasource.OutboundHistoryRemoteDataSource

class DefaultRollOutboundHistoryRepository(
    private val local: OutboundHistoryLocalDataSource,
    private val remote: OutboundHistoryRemoteDataSource,
    private val outboundHistoriesStore: OutboundHistoriesStore,
) : RollOutboundHistoryRepository {

    override fun getOutboundHistories(rollId: Long): Flow<List<OutboundHistory>> {
        return outboundHistoriesStore
            .stream(StoreReadRequest.cached(rollId, refresh = false))
            .filter { it is StoreReadResponse.Data }
            .map { it.requireData() }
    }

    override suspend fun deleteOutboundHistory(id: Long) {
        remote.deleteOutboundHistory(id)
        local.delete(id)
    }

    override suspend fun refresh(rollId: Long) {
        outboundHistoriesStore.fresh(rollId)
    }
}
