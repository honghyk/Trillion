package trillion.wms

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import trillion.wms.di.initKoin

class TrillionWmsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TrillionWmsApplication)
            androidLogger()
        }
    }
}
