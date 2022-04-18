package ba.aadil.namaz.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ba.aadil.namaz.domain.PrayerEvents
import java.time.Instant
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
    @ColumnInfo(name = "region") val region: String?,
) {
    companion object {
        // Sarajevo Id
        const val defaultCityId = 107
    }
}

@Entity(tableName = "prayer_tracking_info")
data class PrayerTrackingInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val prayer: PrayerEvents,
    val completed: Boolean,
    val prayerDateTime: Instant,
    val completedDateTime: Instant,
) {
    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val longDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM, dd, yyyy")
        val hijraFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd")
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val timeFormatterNoSeconds: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}

@Entity(tableName = "badges", indices = [Index(value = ["completedDays"], unique = true)])
data class Badge(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val completedDays: Int,
)


