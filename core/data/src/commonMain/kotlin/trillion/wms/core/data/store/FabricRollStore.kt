package trillion.wms.core.data.store

import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import trillion.wms.core.data.util.storeBuilder
import trillion.wms.core.database.datasource.FabricRollLocalDataSource
import trillion.wms.core.model.FabricRoll
import trillion.wms.core.network.datasource.FabricRollRemoteDataSource

internal class FabricRollStore(
    private val remote: FabricRollRemoteDataSource,
    private val local: FabricRollLocalDataSource,
) : Store<Long, FabricRoll> by storeBuilder(
    fetcher = Fetcher.of { id: Long ->
        remote.getFabricRoll(id)
            ?: throw IllegalArgumentException("FabricRoll with ID $id not found")
    },
    sourceOfTruth = SourceOfTruth.of(
        reader = { id -> local.getFabricRollStream(id) },
        writer = { _, fabricRoll -> local.upsert(fabricRoll) }
    ),
).build()
