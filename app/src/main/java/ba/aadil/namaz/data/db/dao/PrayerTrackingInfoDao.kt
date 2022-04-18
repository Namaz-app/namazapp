package ba.aadil.namaz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.aadil.namaz.data.db.PrayerTrackingInfo
import ba.aadil.namaz.domain.PrayerEvents
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface PrayerTrackingInfoDao {
    @Query("select * from prayer_tracking_info where prayerDateTime=:date and prayer=:prayer")
    fun getPrayerTrackingInfoForDay(prayer: PrayerEvents, date: Instant): List<PrayerTrackingInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrayerTrackingInfo(prayerTrackingInfo: PrayerTrackingInfo)

    @Query("update prayer_tracking_info set completed=:isCompleted, completedDateTime=:completionTime where prayer=:prayer and prayerDateTime=:date")
    fun setPrayerCompleted(
        prayer: PrayerEvents,
        date: Instant,
        isCompleted: Boolean,
        completionTime: Instant,
    )

    @Query("select * from prayer_tracking_info where prayerDateTime>=:startDate and prayerDateTime<=:endDate")
    fun getAllPrayersBetweenTwoDates(
        startDate: Instant,
        endDate: Instant,
    ): List<PrayerTrackingInfo>

    @Query("select * from prayer_tracking_info where prayerDateTime>=:startDate and prayerDateTime<=:endDate")
    fun getAllPrayersBetweenTwoDatesFlow(
        startDate: Instant,
        endDate: Instant,
    ): Flow<List<PrayerTrackingInfo>>
}