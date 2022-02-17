package ba.aadil.namaz.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ba.aadil.namaz.db.PrayerDatabase
import ba.aadil.namaz.db.PrayerDatabase.Companion.MIGRATION_1_2
import ba.aadil.namaz.db.PrayerDatabase.Companion.MIGRATION_2_3
import ba.aadil.namaz.db.PrayerDatabase.Companion.MIGRATION_3_4
import ba.aadil.namaz.db.PrayerDatabase.Companion.MIGRATION_4_5
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<PrayerDatabase> {
        Room.databaseBuilder(
            get(),
            PrayerDatabase::class.java, "prayers.db"
        ).addMigrations(MIGRATION_1_2,
            MIGRATION_2_3,
            MIGRATION_3_4,
            MIGRATION_4_5).createFromAsset("vaktija.db")
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
