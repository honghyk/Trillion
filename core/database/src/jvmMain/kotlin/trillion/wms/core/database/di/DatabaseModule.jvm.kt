package trillion.wms.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import trillion.wms.core.database.AppDatabase
import trillion.wms.core.database.getAppDatabase
import java.io.File


actual val databaseModule: Module = module {
    single {
        getAppDatabase(getAppDatabaseBuilder())
    }
}

private fun getAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(getDatabasePath(), "trillion.db")
    return Room.databaseBuilder(name = dbFile.path)
}

private fun getDatabasePath(): String {
    val appDir = File(getBaseDir(), "TrillionWMS")
    if (!appDir.exists()) {
        appDir.mkdirs()
    }
    return appDir.absolutePath
}

private fun getBaseDir(): String = when {
    isOsWindow() -> System.getenv("LOCALAPPDATA") ?: (getUserHomeDirPath() + "\\AppData\\Local")
    isOsMac() -> getUserHomeDirPath() + "/Library/Application Support"
    else -> getUserHomeDirPath() + "/.local/share"
}

private fun isOsWindow(): Boolean {
    return System.getProperty("os.name").contains("Windows", ignoreCase = true)
}

private fun isOsMac(): Boolean {
    return System.getProperty("os.name").contains("Mac", ignoreCase = true)
}

private fun getUserHomeDirPath(): String {
    return System.getProperty("user.home")
}
