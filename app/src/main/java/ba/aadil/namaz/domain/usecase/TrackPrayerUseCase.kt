package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.domain.PrayerEvents
import ba.aadil.namaz.util.toInstant
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class TrackPrayerUseCase(
    private val prayerTrackingInfoDao: PrayerTrackingInfoDao,
    private val getPrayerTimeForDate: GetPrayerTimeForDate,
) {

    suspend fun updatePrayer(
        prayer: PrayerEvents,
        prayerDate: ZonedDateTime,
        isCompleted: Boolean
    ) {
        prayerTrackingInfoDao.insertPrayerTrackingInfo(
            PrayerTrackingInfo(
                prayerDateTime = prayerDate,
                prayer = prayer,
                isCompleted = isCompleted,
            )
        )
    }
}