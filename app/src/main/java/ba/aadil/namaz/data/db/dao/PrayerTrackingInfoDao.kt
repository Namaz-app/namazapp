package ba.aadil.namaz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.domain.PrayerEvents
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface PrayerTrackingInfoDao {

    @Query("select * from prayer_tracking_info where prayerDateTime=:date and prayer=:prayer")
    suspend fun getPrayerTrackingInfoForDay(prayer: PrayerEvents, date: Instant): List<PrayerTrackingInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTrackingInfo(prayerTrackingInfo: PrayerTrackingInfo)

    @Query("select * from prayer_tracking_info " +
        "where prayerDateTime>=:startDate " +
        "and prayerDateTime<=:endDate " +
        "and isCompleted == 1"
    )
    suspend fun getCompletedPrayersBetweenTwoDates(
        startDate: Instant,
        endDate: Instant,
    ): List<PrayerTrackingInfo>

    @Query("select * from prayer_tracking_info " +
        "where prayerDateTime>=:startDate " +
        "and prayerDateTime<=:endDate " +
        "and isCompleted == 1"
    )
    fun getCompletedPrayersBetweenTwoDatesFlow(
        startDate: Instant,
        endDate: Instant,
    ): Flow<List<PrayerTrackingInfo>>
}