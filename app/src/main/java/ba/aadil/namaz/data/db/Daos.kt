package ba.aadil.namaz.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
interface CityDao {
    @Query("select * from locations")
    fun getAllCities(): List<City>

    @Query("select * from locations where _id == :id")
    fun getCity(id: Int): List<City>
}
