package ba.aadil.namaz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PrayerSchedule::class, CityOffset::class, Track::class], version = 2)
@TypeConverters(Converters::class)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun prayerScheduleDao(): PrayerScheduleDao
    abstract fun offsetDao(): OffsetDao
    abstract fun trackingDao(): TrackingDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Empty implementation, because the schema isn't changing.
                println("PrayerDatabase.migrate prayers")
            }
        }
    }
}

