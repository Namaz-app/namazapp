package ba.aadil.namaz.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ba.aadil.namaz.data.db.PrayerDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<PrayerDatabase> {
        Room.databaseBuilder(
            get(),
            PrayerDatabase::class.java, "prayers.db"
        ).createFromAsset("vaktija.db")
            .build()
    }
    single {
        val db: PrayerDatabase = get()
        db.prayerScheduleDao()
    }
    single {
        val db: PrayerDatabase = get()
        db.offsetDao()
    }
    single {
        get<PrayerDatabase>().trackingDao()
    }
    single {
        get<PrayerDatabase>().badgesDao()
    }
    single {
        get<PrayerDatabase>().cityDao()
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }
}
