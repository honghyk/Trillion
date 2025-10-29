package trillion.wms.core.data.repository.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import trillion.wms.core.model.CreateZoneRequest
import trillion.wms.core.model.UpdateZoneRequest
import trillion.wms.core.model.Zone

interface ZonesRepository {
    fun getZoneStream(id: Long, refresh: Boolean = false): Flow<Zone?>
    fun getZoneByRollIdStream(rollId: Long, refresh: Boolean = false): Flow<Zone?>
    fun getZonesStream(refresh: Boolean = false): Flow<List<Zone>>

    suspend fun createZone(request: CreateZoneRequest)
    suspend fun updateZone(request: UpdateZoneRequest)
    suspend fun deleteZone(id: Long)

    suspend fun refresh(id: Long) = getZoneStream(id, true).first()
}
