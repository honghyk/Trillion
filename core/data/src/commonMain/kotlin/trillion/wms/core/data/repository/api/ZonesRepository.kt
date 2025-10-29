package trillion.wms.core.data.repository.api

import kotlinx.coroutines.flow.Flow
import trillion.wms.core.model.CreateZoneRequest
import trillion.wms.core.model.UpdateZoneRequest
import trillion.wms.core.model.Zone

interface ZonesRepository {
    fun getZoneStream(id: Long): Flow<Zone?>
    fun getZoneByRollIdStream(rollId: Long): Flow<Zone?>
    fun getZonesStream(): Flow<List<Zone>>

    suspend fun createZone(request: CreateZoneRequest)
    suspend fun updateZone(request: UpdateZoneRequest)
    suspend fun deleteZone(id: Long)

    suspend fun refresh(id: Long)
    suspend fun refreshAll()
}
