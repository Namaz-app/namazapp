package ba.aadil.namaz.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ba.aadil.namaz.domain.PrayerEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "prayer_tracking_info")
data class PrayerTrackingInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val prayer: PrayerEvent,
    val date: LocalDate,
    val completed: Boolean,
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