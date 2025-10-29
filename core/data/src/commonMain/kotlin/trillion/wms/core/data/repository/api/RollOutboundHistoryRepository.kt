package trillion.wms.core.data.repository.api

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.model.OutboundHistory

interface RollOutboundHistoryRepository {

    fun getOutboundHistories(
        rollId: Long,
        forceFresh: Boolean
    ): Flow<List<OutboundHistory>>

    suspend fun deleteOutboundHistory(id: Long)
}
