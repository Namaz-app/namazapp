package ba.aadil.namaz.stats

import ba.aadil.namaz.db.TrackingDao
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneOffset

class GetStatisticsUseCase(private val trackingDao: TrackingDao) {
    fun getStatsForPastSevenDays(startDate: LocalDate, endDate: LocalDate): PrayerStatistics {
        val prayed = trackingDao.getAllCompletedPrayersBetweenTwoDates(
            startDate.atStartOfDay().toEpochSecond(ZoneOffset.ofTotalSeconds(0)),
            endDate.atStartOfDay().toEpochSecond(ZoneOffset.ofTotalSeconds(0))
        )
        val totalPrayers = Duration.between(startDate, endDate).toDays().toInt() * 5

        return PrayerStatistics(prayed.size, totalPrayers)
    }

    data class PrayerStatistics(
        val prayedCount: Int,
        val totalCount: Int
    )
}