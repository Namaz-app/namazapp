package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.data.db.PrayerTrackingInfo
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.domain.PrayerEvents
import ba.aadil.namaz.util.toInstant
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class TrackPrayerUseCase(
    private val prayerTrackingInfoDao: PrayerTrackingInfoDao,
    private val getPrayerTimeForDate: GetPrayerTimeForDate,
) {
    suspend fun getOrTrackPrayer(prayer: PrayerEvents, date: LocalDate): PrayerTrackingInfo? {
        val dateFormatted = date.format(PrayerTrackingInfo.dateFormatter)
        val currentData =
            prayerTrackingInfoDao.getPrayerTrackingInfoForDay(prayer, Instant.from(date))
        val prayerDateTime = getPrayerTimeForDate.get(date, prayer).getOrNull()
            ?.toInstant(ZoneOffset.from(Instant.now())) ?: return null


        if (currentData.isEmpty()) {
            prayerTrackingInfoDao.insertPrayerTrackingInfo(
                PrayerTrackingInfo(
                    id = 0,
                    prayer = prayer,
                    completed = false,
                    prayerDateTime = prayerDateTime,
                    Instant.now()
                )
            )
        }

        return prayerTrackingInfoDao.getPrayerTrackingInfoForDay(prayer, date.toInstant())
            .firstOrNull()
    }

    fun togglePrayed(
        prayer: PrayerEvents,
        prayerDate: Instant,
        time: LocalDateTime,
        completed: Boolean
    ) {
        prayerTrackingInfoDao.setPrayerCompleted(
            prayer,
            prayerDate,
            completed,
            time.toInstant(ZoneOffset.ofTotalSeconds(0))
        )
    }
}