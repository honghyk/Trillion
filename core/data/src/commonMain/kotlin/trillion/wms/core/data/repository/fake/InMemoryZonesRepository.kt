package trillion.wms.core.data.repository.fake

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.data.repository.api.ZonesRepository
import trillion.wms.core.model.CreateZoneRequest
import trillion.wms.core.model.UpdateZoneRequest
import trillion.wms.core.model.Zone
import trillion.wms.core.model.ZoneMetrics
import kotlin.time.Clock

class InMemoryZonesRepository(
    private val fabricRollsRepository: FabricRollsRepository,
) : ZonesRepository {
    private var lastGeneratedId: Long = 1L
    private val zones = MutableStateFlow(initialDataSet())

    override fun getZoneStream(id: Long): Flow<Zone?> {
        return zones
            .map { it.firstOrNull { zone -> zone.id == id } }
            .mapLatest { zone -> zone?.let { injectZoneStats(it) } }
    }

    override fun getZoneByRollIdStream(rollId: Long): Flow<Zone?> {
        return fabricRollsRepository.getFabricRoll(rollId)
            .filterNotNull()
            .flatMapLatest { roll -> getZoneStream(roll.zoneId) }
    }

    override fun getZonesStream(): Flow<List<Zone>> {
        return zones.mapLatest { zones ->
            zones.map { zone -> injectZoneStats(zone) }
        }
    }

    private suspend fun injectZoneStats(zone: Zone): Zone {
        val fabricRolls = fabricRollsRepository.getFabricRolls(zone.id).first()
        return zone.copy(
            metrics = ZoneMetrics(
                totalQuantity = fabricRolls.sumOf { it.quantity },
                rollCount = fabricRolls.count(),
            )
        )
    }

    override suspend fun createZone(request: CreateZoneRequest) {
        zones.update {
            if (it.any { zone -> zone.name == request.name }) throw IllegalArgumentException("Zone name already exists")
            it + request.buildZone()
        }
    }

    override suspend fun updateZone(request: UpdateZoneRequest) {
        zones.update { curr ->
            curr.map { zone ->
                if (zone.id == request.id) request.buildZone(zone) else zone
            }
        }
    }

    override suspend fun deleteZone(id: Long) {
        zones.update { curr ->
            curr.filter { it.id != id }
        }
    }

    override suspend fun refresh(id: Long) {
        delay(500L)
    }

    override suspend fun refreshAll() {
        delay(500L)
    }

    private fun CreateZoneRequest.buildZone(): Zone {
        return Zone(
            lastGeneratedId++,
            name = name,
            description = description.orEmpty(),
            createdAt = Clock.System.now(),
        )
    }

    private fun UpdateZoneRequest.buildZone(previous: Zone): Zone {
        return Zone(
            id = id,
            name = name ?: previous.name,
            description = description ?: previous.description,
            createdAt = previous.createdAt,
        )
    }

    private fun initialDataSet(): List<Zone> = listOf(
        Zone(
            lastGeneratedId++,
            "Warehouse A",
            "Main storage area for cotton fabrics",
            Clock.System.now(),
        ),
        Zone(
            lastGeneratedId++,
            "Warehouse B",
            "웨어하우스 B 메모",
            Clock.System.now(),
        ),
        Zone(
            lastGeneratedId++,
            "Cold Storage",
            "Climate-controlled storage for premium fabrics",
            Clock.System.now(),
        ),
    )
}
