package ba.aadil.namaz.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.data.db.model.PrayerTrackingInfo

@Database(
    entities = [PrayerSchedule::class, CityOffset::class, PrayerTrackingInfo::class, Badge::class, City::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(LocalDateConverter::class, PrayerEventsConverter::class, InstantConverter::class)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun prayerScheduleDao(): PrayerScheduleDao
    abstract fun offsetDao(): OffsetDao
    abstract fun trackingDao(): PrayerTrackingInfoDao
    abstract fun badgesDao(): BadgesDao
    abstract fun cityDao(): CityDao
}

