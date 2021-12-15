package ba.aadil.namaz.tracking

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.prayertimes.Events
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TrackPrayerUseCase(private val trackingDao: TrackingDao) {
    suspend fun getOrTrackPrayer(prayer: Events.Prayers, date: LocalDate) {
        val dateFormatted = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val currentData =
            trackingDao.getPrayerForDay(prayer, dateFormatted)
        // todo update prayerDateTime
        if (currentData.isEmpty()) {
            trackingDao.startTracking(
                Track(
                    id = 0,
                    prayer = prayer,
                    completed = false,
                    date = dateFormatted,
                    0,
                    0
                )
            )
        }
    }
}