package ba.aadil.namaz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.domain.PrayerEvent
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface PrayerTrackingInfoDao {
    @Query("select * from prayer_tracking_info where date=:date and prayer=:prayer")
    fun getPrayerTrackingInfoForDay(
        prayer: PrayerEvent,
        date: Instant
    ): List<PrayerTrackingInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrayerTrackingInfo(prayerTrackingInfo: PrayerTrackingInfo)

    @Query("update prayer_tracking_info set completed=:isCompleted where prayer=:prayer and date=:date")
    fun setPrayerCompleted(
        prayer: PrayerEvent,
        date: Instant,
        isCompleted: Boolean,
    )

    @Query("select * from prayer_tracking_info where date>=:startDate and date<=:endDate")
    fun getAllPrayersBetweenTwoDates(
        startDate: Instant,
        endDate: Instant,
    ): List<PrayerTrackingInfo>

    @Query("select * from prayer_tracking_info where date>=:startDate and date<=:endDate")
    fun getAllPrayersBetweenTwoDatesFlow(
        startDate: Instant,
        endDate: Instant,
    ): Flow<List<PrayerTrackingInfo>>
}