package ba.aadil.namaz.stats

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.prayertimes.Events
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
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
        val days =
            Duration.between(startDate.atStartOfDay(), endDatePlusDay.atStartOfDay()).toDays()
                .toInt()
        val totalPrayers = days * 5
        val completedMap = hashMapOf<Events.Prayers, Int>()
        prayed.filter { track -> track.completed }.forEach { track ->
            completedMap[track.prayer] = (completedMap[track.prayer] ?: 0) + 1
        }
        val prayerStats = completedMap.keys.map { key ->
            SinglePrayerStats(key, completedMap[key] ?: 0, days)
        }

        return PrayerStatistics(prayed, prayerStats, totalPrayers)
    }

    fun getStatsBetweenDaysLive(startDate: LocalDate, endDate: LocalDate): Flow<PrayerStatistics> {
        val endDatePlusDay = endDate.plusDays(1)
        val durationDays =
            Duration.between(startDate.atStartOfDay(), endDatePlusDay.atStartOfDay()).toDays()
                .toInt()
        val totalPrayers = durationDays * 5
        return trackingDao.getAllCompletedPrayersBetweenTwoDatesFlow(
            startDate.atStartOfDay().toEpochSecond(ZoneOffset.ofTotalSeconds(0)),
            endDatePlusDay.atStartOfDay().toEpochSecond(ZoneOffset.ofTotalSeconds(0))
        ).map { tracks ->
            val completedMap = hashMapOf<Events.Prayers, Int>()
            tracks.filter { track -> track.completed }.forEach { track ->
                completedMap[track.prayer] = (completedMap[track.prayer] ?: 0) + 1
            }
            val prayerStats = completedMap.keys.map { key ->
                SinglePrayerStats(key, completedMap[key] ?: 0, totalPrayers)
            }
            PrayerStatistics(tracks, prayerStats, totalPrayers)
        }
    }

    data class TotalPrayedStats(
        val trackedPrayers: List<Track>,
        val totalCount: Int,
    )

    data class PrayerStatistics(
        val trackedPrayers: List<Track>,
        val states: List<SinglePrayerStats>,
        val totalCount: Int,
    )

    data class SinglePrayerStats(
        val type: Events.Prayers,
        val prayedCount: Int,
        val totalCount: Int,
    ) : ViewModel
}