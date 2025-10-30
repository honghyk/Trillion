package trillion.wms.core.data.repository.api

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundRequest
import trillion.wms.core.model.UpdateFabricRollRequest

interface FabricRollsRepository {
    fun getFabricRoll(id: Long): Flow<FabricRoll?>

    fun getFabricRolls(
        zoneId: Long,
        forceFresh: Boolean = false
    ): Flow<List<FabricRoll>>

    suspend fun addFabricRoll(request: AddFabricRollRequest): FabricRoll
    suspend fun updateFabricRoll(request: UpdateFabricRollRequest): FabricRoll
    suspend fun deleteFabricRoll(id: Long)
    suspend fun outboundFabricRoll(request: OutboundRequest)

    suspend fun refresh(id: Long)
    suspend fun refreshAllInZone(zoneId: Long)
}
