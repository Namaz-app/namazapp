package ba.aadil.namaz.stats

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneOffset

class GetStatisticsUseCase(private val trackingDao: TrackingDao) {
    fun getStatsBetweenDays(startDate: LocalDate, endDate: LocalDate): PrayerStatistics {
        val endDatePlusDay = endDate.plusDays(1)

        val prayed = trackingDao.getAllCompletedPrayersBetweenTwoDates(
            startDate.atStartOfDay().toEpochSecond(ZoneOffset.ofTotalSeconds(0)),
            endDatePlusDay.atStartOfDay().toEpochSecond(ZoneOffset.ofTotalSeconds(0))
        )
        val totalPrayers =
            Duration.between(startDate.atStartOfDay(), endDatePlusDay.atStartOfDay()).toDays()
                .toInt() * 5

        return PrayerStatistics(prayed, totalPrayers)
    }

    fun getStatsBetweenDaysLive(startDate: LocalDate, endDate: LocalDate): Flow<PrayerStatistics> {
        val endDatePlusDay = endDate.plusDays(1)
        val totalPrayers =
            Duration.between(startDate.atStartOfDay(), endDatePlusDay.atStartOfDay()).toDays()
                .toInt() * 5
        return trackingDao.getAllCompletedPrayersBetweenTwoDatesFlow(
            startDate.atStartOfDay().toEpochSecond(ZoneOffset.ofTotalSeconds(0)),
            endDatePlusDay.atStartOfDay().toEpochSecond(ZoneOffset.ofTotalSeconds(0))
        ).map {
            PrayerStatistics(it, totalPrayers)
        }
    }

    data class PrayerStatistics(
        val trackedPrayers: List<Track>,
        val totalCount: Int
    )
}