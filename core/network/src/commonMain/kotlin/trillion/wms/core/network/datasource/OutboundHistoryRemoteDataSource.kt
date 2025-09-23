package trillion.wms.core.network.datasource

import co.touchlab.kermit.Logger
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.network.api.OutboundHistoryApi
import trillion.wms.core.network.model.toDomain

interface OutboundHistoryRemoteDataSource {
    suspend fun getOutboundHistoriesForRoll(rollId: Long): List<OutboundHistory>
    suspend fun getAllOutboundHistories(): List<OutboundHistory>
    suspend fun deleteOutboundHistory(id: Long): OutboundHistory?
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

    override suspend fun deleteOutboundHistory(id: Long): OutboundHistory? {
        return outboundHistoryApi.deleteOutboundHistory(id)?.toDomain()
    }
}
