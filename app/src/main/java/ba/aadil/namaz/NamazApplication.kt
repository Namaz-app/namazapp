package ba.aadil.namaz

import android.app.Application
import ba.aadil.namaz.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NamazApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@NamazApplication)
            modules(dataModule)
        }
    }
}