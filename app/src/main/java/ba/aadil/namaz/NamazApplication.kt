package ba.aadil.namaz

import android.app.Application
import ba.aadil.namaz.di.dataModule
import ba.aadil.namaz.di.domainModule
import ba.aadil.namaz.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NamazApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@NamazApplication)
            modules(dataModule, domainModule, presentationModule)
        }
    }
}