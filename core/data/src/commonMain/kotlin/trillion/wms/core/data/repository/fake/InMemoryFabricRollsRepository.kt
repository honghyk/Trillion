package trillion.wms.core.data.repository.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import trillion.wms.core.data.repository.api.FabricRollsRepository
import trillion.wms.core.model.AddFabricRollRequest
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.model.OutboundHistory
import trillion.wms.core.model.OutboundRequest
import trillion.wms.core.model.UpdateFabricRollRequest
import trillion.wms.core.model.exception.AlreadyExistsException
import kotlin.math.round
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant

class InMemoryFabricRollsRepository : FabricRollsRepository {
    private var lastGeneratedId: Long = 1L
    private var lastGeneratedHistoryId: Long = 1L
    private val fabricRolls = MutableStateFlow(generateFakeFabricRolls(1000))
    private val outboundHistories = MutableStateFlow<List<OutboundHistory>>(emptyList())

    override fun getFabricRoll(id: Long, forceFresh: Boolean): Flow<FabricRoll?> {
        return fabricRolls.map { rolls -> rolls.firstOrNull { it.id == id } }
    }

    override fun getFabricRolls(zoneId: Long, forceFresh: Boolean): Flow<List<FabricRoll>> {
        return fabricRolls
            .map { rolls -> rolls.filter { it.zoneId == zoneId } }
    }

    override fun getAllFabricRolls(forceFresh: Boolean): Flow<List<FabricRoll>> {
        return fabricRolls
    }

    override suspend fun addFabricRoll(request: AddFabricRollRequest): FabricRoll {
        val newFabricRoll = request.toFabricRoll()
        fabricRolls.update { currentRolls ->
            if (currentRolls.any { it.id == request.id }) {
                throw AlreadyExistsException("Fabric roll with ID ${request.id} already exists.")
            }
            currentRolls + newFabricRoll
        }
        return newFabricRoll
    }

    override suspend fun updateFabricRoll(request: UpdateFabricRollRequest): FabricRoll {
        var updatedFabricRoll: FabricRoll? = null
        fabricRolls.update { currentRolls ->
            currentRolls.map {
                if (it.id == request.id) {
                    updatedFabricRoll = it.update(request)
                    updatedFabricRoll
                } else {
                    it
                }
            }
        }
        return updatedFabricRoll ?: throw NoSuchElementException()
    }

    override suspend fun deleteFabricRoll(id: Long) {
        fabricRolls.update { it.filter { roll -> roll.id != id } }
    }

    override suspend fun outboundFabricRoll(request: OutboundRequest) {
        fabricRolls.value = fabricRolls.value.map { roll ->
            if (roll.id == request.rollId) {
                roll.copy(remainingQuantity = roll.remainingQuantity - request.qtyToProcess)
            } else {
                roll
            }
        }
        outboundHistories.update {
            it + OutboundHistory(
                id = lastGeneratedHistoryId++,
                rollId = request.rollId,
                quantity = request.qtyToProcess,
                buyer = request.buyer,
                remark = request.remark.orEmpty(),
                createdAt = Clock.System.now()
            )
        }
    }

    private fun AddFabricRollRequest.toFabricRoll() = FabricRoll(
        id = id,
        zoneId = zoneId,
        itemNo = itemNo,
        orderNo = orderNo.orEmpty(),
        color = color.orEmpty(),
        factory = factory.orEmpty(),
        finish = finish.orEmpty(),
        remark = remark.orEmpty(),
        quantity = quantity,
        remainingQuantity = quantity,
        createdAt = Clock.System.now()
    )

    private fun FabricRoll.update(request: UpdateFabricRollRequest) = copy(
        zoneId = request.zoneId ?: zoneId,
        itemNo = request.itemNo ?: itemNo,
        orderNo = request.orderNo ?: orderNo,
        color = request.color ?: color,
        factory = request.factory ?: factory,
        finish = request.finish ?: finish,
        remark = request.remark ?: remark,
        quantity = request.quantity ?: quantity,
    )

    private fun generateFakeFabricRolls(count: Int): List<FabricRoll> {
        val colors = listOf(
            "Natural White", "Emerald Green", "Indigo Blue", "Charcoal Gray", "Cream",
            "Royal Blue", "Crimson Red", "Jet Black", "Heather Gray", "Beige", "Olive Drab"
        )
        val factories = listOf(
            "TextileCorp Manufacturing", "LuxeFabrics Ltd", "DenimWorks Inc",
            "SyntheticTextiles Co", "NaturalFibers LLC", "Global Weavers"
        )
        val finishes = listOf(
            "Organic", "Delicate", "Stone Wash", "Wrinkle-resistant", "Premium",
            "Brushed", "Sateen", null
        )
        val materialCodes = listOf("CTN", "SLK", "DNM", "PLY", "LIN", "VLV", "WOL", "RAY")

        val rolls = mutableListOf<FabricRoll>()
        val random = Random(Clock.System.now().epochSeconds)

        for (i in 1..count) {
            val material = materialCodes.random(random)
            val itemNo = "$material-${i.toString().padStart(3, '0')}"
            val hasOrder = random.nextBoolean()
            val orderNo = if (hasOrder) "ORD-2024-${i.toString().padStart(4, '0')}" else null
            val quantity = random.nextDouble(50.0, 2000.0).roundTo(2)
            val factory = factories.random(random)

            // Generate a random timestamp within the last year
            val now = Clock.System.now().epochSeconds
            val oneYearAgo = now - (365 * 24 * 60 * 60)
            val randomTimestamp = random.nextLong(oneYearAgo, now)

            rolls.add(
                FabricRoll(
                    id = lastGeneratedId++,
                    zoneId = random.nextLong(1, 4), // Assuming 3 zones
                    itemNo = itemNo,
                    orderNo = orderNo.orEmpty(),
                    color = colors.random(random),
                    factory = factory,
                    finish = finishes.random(random).orEmpty(),
                    remark = if (random.nextDouble() > 0.7) "Remark for item #$i" else "",
                    quantity = quantity,
                    remainingQuantity = quantity,
                    createdAt = Instant.fromEpochSeconds(randomTimestamp)
                )
            )
        }
        return rolls
    }

    private fun Double.roundTo(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}
