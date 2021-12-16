package ba.aadil.namaz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ba.aadil.namaz.prayertimes.Events
import java.time.format.DateTimeFormatter

@Entity(tableName = "schedule")
data class PrayerSchedule(
    @PrimaryKey val _id: Int?,
    @ColumnInfo(name = "datum") val date: String?,
    @ColumnInfo(name = "fajr") val morningPrayer: String,
    @ColumnInfo(name = "sunrise") val sunrise: String,
    @ColumnInfo(name = "dhuhr") val noonPrayer: String,
    @ColumnInfo(name = "asr") val afternoonPrayer: String,
    @ColumnInfo(name = "maghrib") val sunsetPrayer: String,
    @ColumnInfo(name = "isha") val nightPrayer: String,
)

@Entity(tableName = "offset")
data class CityOffset(
    @PrimaryKey val _id: Int?,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "fajr") val morningPrayerOffset: Int,
    @ColumnInfo(name = "dhuhr") val noonPrayerOffset: Int,
    @ColumnInfo(name = "asr") val afternoonPrayerOffset: Int,
)

@Entity(tableName = "locations")
data class City(
    @PrimaryKey val _id: Int?,
    @ColumnInfo(name = "location") val name: String,
    @ColumnInfo(name = "weight") val generalPrayerOffset: Int,
)

@Entity(tableName = "tracking")
data class Track(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val prayer: Events.Prayers,
    val completed: Boolean,
    val date: String,
    val prayerDateTime: Long,
    val completedDateTime: Long
) {
    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }
}


