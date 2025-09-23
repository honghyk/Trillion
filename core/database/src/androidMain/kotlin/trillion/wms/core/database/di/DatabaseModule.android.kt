package trillion.wms.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import trillion.wms.core.database.AppDatabase
import trillion.wms.core.database.getAppDatabase

actual val databaseModule: Module = module {
    single {
        getAppDatabase(getAppDatabaseBuilder(androidContext()))
    }
}

private fun getAppDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("trillion.db")
    return Room.databaseBuilder(
        appContext,
        AppDatabase::class.java, dbFile.path
    )
}
