package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.data.db.Track
import ba.aadil.namaz.data.db.TrackingDao
import ba.aadil.namaz.domain.Events
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class TrackPrayerUseCase(
    private val trackingDao: TrackingDao,
    private val getPrayerTimeForDate: GetPrayerTimeForDate,
) {
    suspend fun getOrTrackPrayer(prayer: Events.Prayers, date: LocalDate): Track? {
        val dateFormatted = date.format(Track.dateFormatter)
        val currentData =
            trackingDao.getPrayerForDay(prayer, dateFormatted)
        val prayerDateTime =
            getPrayerTimeForDate.get(date, prayer).getOrNull()?.toEpochSecond(ZoneOffset.ofTotalSeconds(0))
                ?: 0

        if (currentData.isEmpty()) {
            trackingDao.startTracking(
                Track(
                    id = 0,
                    prayer = prayer,
                    completed = false,
                    date = dateFormatted,
                    prayerDateTime = prayerDateTime,
                    0
                )
            )
        }

        return trackingDao.getPrayerForDay(prayer, dateFormatted).firstOrNull()
    }

    fun togglePrayed(
        prayer: Events.Prayers,
        prayerDate: String,
        time: LocalDateTime,
        completed: Boolean
    ) {
        trackingDao.togglePrayerCompletion(
            prayer,
            prayerDate,
            completed,
            time.toEpochSecond(ZoneOffset.ofTotalSeconds(0))
        )
    }
}