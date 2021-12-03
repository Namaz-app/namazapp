package ba.aadil.namaz.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PrayerScheduleDao::class, OffsetDao::class], version = 2)
public abstract class PrayerDatabase : RoomDatabase() {
    abstract fun prayerScheduleDao(): PrayerScheduleDao
    abstract fun offsetDao(): OffsetDao
}