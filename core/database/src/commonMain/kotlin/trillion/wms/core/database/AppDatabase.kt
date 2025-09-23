package trillion.wms.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import trillion.wms.core.database.dao.FabricRollDao
import trillion.wms.core.database.dao.OutboundHistoryDao
import trillion.wms.core.database.dao.ZoneDao
import trillion.wms.core.database.model.FabricRollEntity
import trillion.wms.core.database.model.OutboundHistoryEntity
import trillion.wms.core.database.model.ZoneEntity
import trillion.wms.core.database.util.InstantConverter

@Database(
    entities = [
        ZoneEntity::class,
        FabricRollEntity::class,
        OutboundHistoryEntity::class,
    ],
    version = 1
)
@TypeConverters(
    InstantConverter::class
)
@ConstructedBy(AppDatabaseConstructor::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun zoneDao(): ZoneDao
    abstract fun fabricRollDao(): FabricRollDao
    abstract fun outboundHistoryDao(): OutboundHistoryDao
}

@Suppress("KotlinNoActualForExpect")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

internal fun getAppDatabase(
    builder: RoomDatabase.Builder<AppDatabase>,
): AppDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
