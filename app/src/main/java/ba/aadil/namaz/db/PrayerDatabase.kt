package ba.aadil.namaz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [PrayerSchedule::class, CityOffset::class, Track::class],
    version = 3,
    exportSchema = true
)
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
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Empty implementation, because the schema isn't changing.
                database.execSQL("CREATE TABLE IF NOT EXISTS `tracking` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `prayer` INTEGER NOT NULL, `completed` INTEGER NOT NULL, `date` TEXT NOT NULL, `prayerDateTime` INTEGER NOT NULL, `completedDateTime` INTEGER NOT NULL)")
            }
        }
    }
}

