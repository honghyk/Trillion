package trillion.wms.core.data.util

import org.mobilenativefoundation.store.store5.Converter
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder

fun <Key : Any, Model : Any> storeBuilder(
    fetcher: Fetcher<Key, Model>,
    sourceOfTruth: SourceOfTruth<Key, Model, Model>,
): StoreBuilder<Key, Model> = StoreBuilder.from(
    fetcher,
    sourceOfTruth,
    Converter.Builder<Model, Model, Model>()
        .fromOutputToLocal { it }
        .fromNetworkToLocal { it }
        .build()
)
