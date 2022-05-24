package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.domain.PrayerEvent
import ba.aadil.namaz.util.toInstant
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class TrackPrayerUseCase(
    private val prayerTrackingInfoDao: PrayerTrackingInfoDao,
    private val getPrayerTimeForDate: GetPrayerTimeForDate,
) {
    suspend fun getOrTrackPrayer(prayer: PrayerEvent, date: LocalDate): PrayerTrackingInfo? {
        val currentData =
            prayerTrackingInfoDao.getPrayerTrackingInfoForDay(prayer, date.toInstant())
        val prayerDateTime = getPrayerTimeForDate.get(date, prayer).getOrNull()
            ?.toInstant(ZoneOffset.UTC) ?: return null

        return prayerTrackingInfoDao.getPrayerTrackingInfoForDay(prayer, date.toInstant())
            .firstOrNull()
    }

    fun togglePrayed(
        prayer: PrayerEvent,
        prayerDate: Instant,
        completed: Boolean
    ) {
        prayerTrackingInfoDao.setPrayerCompleted(
            prayer,
            prayerDate,
            completed
        )
    }
}