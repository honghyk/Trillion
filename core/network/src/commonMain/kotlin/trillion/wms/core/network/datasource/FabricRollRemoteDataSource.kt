package trillion.wms.core.network.datasource

import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.model.OutboundRequest
import trillion.wms.core.model.UpdateFabricRollRequest
import trillion.wms.core.model.exception.AlreadyExistsException
import trillion.wms.core.network.api.FabricRollApi
import trillion.wms.core.network.model.toDomain
import trillion.wms.core.network.model.toInsertPayload
import trillion.wms.core.network.model.toRpcParams
import trillion.wms.core.network.model.toUpdatePayload

interface FabricRollRemoteDataSource {
    suspend fun getFabricRolls(zoneId: Long? = null): List<FabricRoll>
    suspend fun getFabricRollById(id: Long): FabricRoll?
    suspend fun addFabricRoll(request: AddFabricRollRequest): FabricRoll
    suspend fun updateFabricRoll(request: UpdateFabricRollRequest): FabricRoll // Added update method
    suspend fun outboundFabricRoll(request: OutboundRequest): OutboundHistory
    suspend fun deleteFabricRoll(rollId: Long)
    suspend fun searchFabricRolls(query: String, zoneId: Long?): List<FabricRoll>
}

internal class DefaultFabricRollRemoteDataSource(
    private val fabricRollApi: FabricRollApi
) : FabricRollRemoteDataSource {

    override suspend fun getFabricRolls(zoneId: Long?): List<FabricRoll> {
        return fabricRollApi.getFabricRolls(zoneId).toDomain()
    }

    override suspend fun getFabricRollById(id: Long): FabricRoll? {
        return fabricRollApi.getFabricRollById(id)?.toDomain()
    }

    override suspend fun addFabricRoll(request: AddFabricRollRequest): FabricRoll {
        return try {
            fabricRollApi
                .addFabricRoll(request.toInsertPayload())
                .toDomain()
        } catch (e: PostgrestRestException) {
            when {
                e.statusCode == 409 -> throw AlreadyExistsException()
                else -> throw e
            }
        }
    }

    override suspend fun updateFabricRoll(request: UpdateFabricRollRequest): FabricRoll {
        val payload = request.toUpdatePayload()
        return fabricRollApi.updateFabricRoll(request.id, payload).toDomain()
    }

    override suspend fun outboundFabricRoll(request: OutboundRequest): OutboundHistory {
        val params = request.toRpcParams()
        return fabricRollApi.outboundFabricRoll(params).toDomain()
    }

    override suspend fun deleteFabricRoll(rollId: Long) {
        fabricRollApi.deleteFabricRoll(rollId)
    }

    override suspend fun searchFabricRolls(query: String, zoneId: Long?): List<FabricRoll> {
        return fabricRollApi.searchFabricRolls(query, zoneId).toDomain()
    }
}
