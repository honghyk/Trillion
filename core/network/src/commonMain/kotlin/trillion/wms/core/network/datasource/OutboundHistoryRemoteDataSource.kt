package trillion.wms.core.network.datasource

import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.network.api.OutboundHistoryApi
import trillion.wms.core.network.model.toDomain

interface OutboundHistoryRemoteDataSource {
    suspend fun getOutboundHistoriesForRoll(rollId: Long): List<OutboundHistory>
    suspend fun getAllOutboundHistories(): List<OutboundHistory>
    suspend fun deleteOutboundHistory(id: Long)
}

internal class DefaultOutboundHistoryRemoteDataSource(
    private val outboundHistoryApi: OutboundHistoryApi
) : OutboundHistoryRemoteDataSource {

    override suspend fun getOutboundHistoriesForRoll(rollId: Long): List<OutboundHistory> {
        return outboundHistoryApi.getOutboundHistoriesForRoll(rollId).toDomain()
    }

    override suspend fun getAllOutboundHistories(): List<OutboundHistory> {
        return outboundHistoryApi.getAllOutboundHistories().toDomain()
    }

    override suspend fun deleteOutboundHistory(id: Long) {
        return outboundHistoryApi.deleteOutboundHistory(id)
    }
}
