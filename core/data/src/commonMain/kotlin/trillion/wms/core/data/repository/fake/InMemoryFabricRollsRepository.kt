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

    override fun getFabricRollStream(id: Long, forceRefresh: Boolean): Flow<FabricRoll?> {
        return fabricRolls.map { rolls -> rolls.firstOrNull { it.id == id } }
    }

    override fun getFabricRollsStream(zoneId: Long, forceRefresh: Boolean): Flow<List<FabricRoll>> {
        return fabricRolls
            .map { rolls -> rolls.filter { it.zoneId == zoneId } }
    }

    override fun getAllFabricRollsStream(forceRefresh: Boolean): Flow<List<FabricRoll>> {
        return fabricRolls
    }

    override suspend fun addFabricRoll(request: AddFabricRollRequest) {
        fabricRolls.update {
            if (it.any { roll -> roll.id == request.id }) throw AlreadyExistsException()
            it + FabricRoll(
                id = request.id,
                zoneId = request.zoneId,
                itemNo = request.itemNo,
                orderNo = request.orderNo.orEmpty(),
                color = request.color.orEmpty(),
                factory = request.factory.orEmpty(),
                finish = request.finish.orEmpty(),
                remark = request.remark.orEmpty(),
                quantity = request.quantity,
                remainingQuantity = request.quantity,
                createdAt = Clock.System.now()
            )
        }
    }

    override suspend fun updateFabricRoll(request: UpdateFabricRollRequest) {
        fabricRolls.update {
            it.map { roll ->
                if (roll.id == request.id) {
                    roll.copy(
                        zoneId = request.zoneId ?: roll.zoneId,
                        itemNo = request.itemNo ?: roll.itemNo,
                        orderNo = request.orderNo ?: roll.orderNo,
                        color = request.color ?: roll.color,
                        factory = request.factory ?: roll.factory,
                        finish = request.finish ?: roll.finish,
                        remark = request.remark ?: roll.remark,
                        quantity = request.quantity ?: roll.quantity,
                    )
                } else {
                    roll
                }
            }
        }
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

    override fun getOutboundHistoryStream(
        rollId: Long,
        forceRefresh: Boolean
    ): Flow<List<OutboundHistory>> {
        return outboundHistories.map { it.filter { history -> history.rollId == rollId } }
    }

    override suspend fun deleteOutboundHistory(id: Long) {
        outboundHistories.update { it.filter { history -> history.id != id } }
    }

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
