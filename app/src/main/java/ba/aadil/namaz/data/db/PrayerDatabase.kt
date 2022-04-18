package ba.aadil.namaz.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao

@Database(
    entities = [PrayerSchedule::class, CityOffset::class, PrayerTrackingInfo::class, Badge::class, City::class],
    version = 5,
    exportSchema = true
)
@TypeConverters(Converters::class, PrayerEventsConverter::class)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun prayerScheduleDao(): PrayerScheduleDao
    abstract fun offsetDao(): OffsetDao
    abstract fun trackingDao(): PrayerTrackingInfoDao
    abstract fun badgesDao(): BadgesDao
    abstract fun cityDao(): CityDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Empty implementation, because the schema isn't changing.
                println("PrayerDatabase.migrate prayers")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `tracking` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `prayer` INTEGER NOT NULL, `completed` INTEGER NOT NULL, `date` TEXT NOT NULL, `prayerDateTime` INTEGER NOT NULL, `completedDateTime` INTEGER NOT NULL)")
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `badges` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `completedDays` INTEGER NOT NULL)")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_badges_completedDays` ON `badges` (`completedDays`)")
            }
        }
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `locations` (`_id` INTEGER, `location` TEXT NOT NULL, `weight` TEXT, PRIMARY KEY(`_id`))")
            }
        }
    }
}

