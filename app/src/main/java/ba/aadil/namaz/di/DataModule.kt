package ba.aadil.namaz.di

import androidx.room.Room
import ba.aadil.namaz.db.PrayerDatabase
import ba.aadil.namaz.db.PrayerDatabase.Companion.MIGRATION_1_2
import org.koin.dsl.module

val dataModule = module {
    single<PrayerDatabase> {
        Room.databaseBuilder(
            get(),
            PrayerDatabase::class.java, "prayers.db"
        ).addMigrations(MIGRATION_1_2).createFromAsset("vaktija.db").build()
    }
}
