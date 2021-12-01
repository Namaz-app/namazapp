package ba.aadil.namaz.ui.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class PrayerSchedule(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "datum") val date: String,
    @ColumnInfo(name = "fajr") val morningPrayer: String,
    @ColumnInfo(name = "sunrise") val sunrise: String,
    @ColumnInfo(name = "dhuhr") val noonPrayer: String,
    @ColumnInfo(name = "asr") val afternoonPrayer: String,
    @ColumnInfo(name = "maghrib") val sunsetPrayer: String,
    @ColumnInfo(name = "isha") val nightPrayer: String,
)

@Entity(tableName = "offset")
data class CityOffset(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "fajr") val morningPrayerOffset: Int,
    @ColumnInfo(name = "dhuhr") val noonPrayerOffset: Int,
    @ColumnInfo(name = "asr") val afternoonPrayerOffset: Int,
)

@Entity(tableName = "locations")
data class City(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "location") val name: String,
    @ColumnInfo(name = "weight") val generalPrayerOffset: Int,
)
