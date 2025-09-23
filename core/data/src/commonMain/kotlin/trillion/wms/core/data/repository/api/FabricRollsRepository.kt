package trillion.wms.core.data.repository.api

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.model.OutboundRequest
import trillion.wms.core.model.UpdateFabricRollRequest

interface FabricRollsRepository {
    fun getFabricRollStream(id: Long, forceRefresh: Boolean = false): Flow<FabricRoll?>
    fun getFabricRollsStream(zoneId: Long, forceRefresh: Boolean = false): Flow<List<FabricRoll>>
    fun getAllFabricRollsStream(forceRefresh: Boolean = false): Flow<List<FabricRoll>>
    suspend fun addFabricRoll(request: AddFabricRollRequest)
    suspend fun updateFabricRoll(request: UpdateFabricRollRequest)
    suspend fun deleteFabricRoll(id: Long)
    suspend fun outboundFabricRoll(request: OutboundRequest)

    fun getOutboundHistoryStream(
        rollId: Long,
        forceRefresh: Boolean = false
    ): Flow<List<OutboundHistory>>

    suspend fun deleteOutboundHistory(id: Long)
}
