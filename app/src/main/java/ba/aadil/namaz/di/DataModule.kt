package ba.aadil.namaz.di

import androidx.room.Room
import ba.aadil.namaz.db.PrayerDatabase
import ba.aadil.namaz.db.PrayerDatabase.Companion.MIGRATION_1_2
import ba.aadil.namaz.db.PrayerDatabase.Companion.MIGRATION_2_3
import ba.aadil.namaz.db.PrayerDatabase.Companion.MIGRATION_3_4
import org.koin.dsl.module

val dataModule = module {
    single<PrayerDatabase> {
        Room.databaseBuilder(
            get(),
            PrayerDatabase::class.java, "prayers.db"
        ).addMigrations(MIGRATION_1_2,
            MIGRATION_2_3,
            MIGRATION_3_4).createFromAsset("vaktija.db")
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
}
