package ba.aadil.namaz.tracking

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.prayertimes.Events
import java.time.LocalDate

class TrackPrayerUseCase(private val trackingDao: TrackingDao) {
    suspend fun getOrTrackPrayer(prayer: Events.Prayers, date: LocalDate): Track? {
        val dateFormatted = date.format(Track.dateFormatter)
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

        return trackingDao.getPrayerForDay(prayer, dateFormatted).firstOrNull()
    }
}