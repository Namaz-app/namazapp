package ba.aadil.namaz.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.aadil.namaz.prayertimes.Events

@Dao
interface PrayerScheduleDao {
    @Query("select * from schedule where datum=:date")
    fun getPrayersForADay(date: String): List<PrayerSchedule>
}

@Dao
interface OffsetDao {
    @Query("select * from `offset` where month=:month and location_id=:locationId")
    fun getOffsetForACity(month: Int, locationId: Int): List<CityOffset>
}

@Dao
interface TrackingDao {
    @Query("select * from tracking where date=:date and prayer=:prayer")
    fun getPrayerForDay(prayer: Events.Prayers, date: String): List<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun startTracking(track: Track)

    @Query("update tracking set completed=:completed where prayer=:prayer and date=:date")
    fun togglePrayerCompletion(prayer: Events.Prayers, completed: Boolean, date: String)
}