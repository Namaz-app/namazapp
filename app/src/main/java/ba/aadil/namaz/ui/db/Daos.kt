package ba.aadil.namaz.ui.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PrayerScheduleDao {
    @Query("select * from schedule where datum=datum")
    fun getPrayersForADay(date: String): List<PrayerSchedule>
}

@Dao
interface OffsetDao {
    @Query("select * from `offset` where month=month and location_id=location_id")
    fun getOffsetForACity(month: Int, locationId: Int): List<CityOffset>
}
