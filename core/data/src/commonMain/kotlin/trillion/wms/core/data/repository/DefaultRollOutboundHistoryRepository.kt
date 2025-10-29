package trillion.wms.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import trillion.wms.core.data.repository.api.RollOutboundHistoryRepository
import trillion.wms.core.database.datasource.OutboundHistoryLocalDataSource
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.network.datasource.OutboundHistoryRemoteDataSource

class DefaultRollOutboundHistoryRepository(
    private val local: OutboundHistoryLocalDataSource,
    private val remote: OutboundHistoryRemoteDataSource,
) : RollOutboundHistoryRepository {

    override fun getOutboundHistories(
        rollId: Long,
        forceFresh: Boolean
    ): Flow<List<OutboundHistory>> = flow {
        val localDataFlow = local.getOutboundHistories(rollId)
        val currentLocalData = localDataFlow.first()

        if (forceFresh || currentLocalData.isEmpty()) {
            try {
                val remoteRollHistories = remote.getOutboundHistoriesForRoll(rollId)
                if (forceFresh) {
                    local.deleteByRollId(rollId)
                }
                local.insertAll(remoteRollHistories)
            } catch (e: Exception) {
                if (currentLocalData.isEmpty()) throw e
            }
        }
        emitAll(localDataFlow)
    }

    override suspend fun deleteOutboundHistory(id: Long) {
        remote.deleteOutboundHistory(id)
        local.delete(id)
    }
}
