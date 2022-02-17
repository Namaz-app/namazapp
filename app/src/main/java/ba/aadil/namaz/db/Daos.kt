package ba.aadil.namaz.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.aadil.namaz.prayertimes.Events
import kotlinx.coroutines.flow.Flow

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
interface BadgesDao {
    @Query("select * from badges")
    fun getAllBadges(): Flow<List<Badge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeBadge(badge: Badge)
}

@Dao
interface TrackingDao {
    @Query("select * from tracking where date=:date and prayer=:prayer")
    fun getPrayerForDay(prayer: Events.Prayers, date: String): List<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun startTracking(track: Track)

    @Query("update tracking set completed=:completed, completedDateTime=:completionTime where prayer=:prayer and date=:date")
    fun togglePrayerCompletion(
        prayer: Events.Prayers,
        date: String,
        completed: Boolean,
        completionTime: Long,
    )

    @Query("select * from tracking where completedDateTime>=:startMillis and completedDateTime<=:endMillis")
    fun getAllCompletedPrayersBetweenTwoDates(startMillis: Long, endMillis: Long): List<Track>

    @Query("select * from tracking where prayerDateTime>=:startEpoch and prayerDateTime<=:endEpoch")
    fun getAllCompletedPrayersBetweenTwoDatesFlow(
        startEpoch: Long,
        endEpoch: Long,
    ): Flow<List<Track>>
}

@Dao
interface CityDao {
    @Query("select * from locations")
    fun getAllCities(): List<City>
}
