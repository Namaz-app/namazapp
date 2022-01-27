package ba.aadil.namaz.user

import ba.aadil.namaz.db.TrackingDao
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class GetBadges(val trackingDao: TrackingDao) {
    fun getAllBadges(): List<ConsecutivePrayerBadge> {
        val badges = mutableListOf<ConsecutivePrayerBadge>()
        listOf(1, 7, 14, 21, 28, 35, 42).forEach { pastDays ->
            val endDate = LocalDateTime.now().toEpochSecond(ZoneOffset.ofTotalSeconds(0))
            val startDate =
                LocalDate.now().minusDays(pastDays.toLong()).atStartOfDay()
                    .toEpochSecond(ZoneOffset.ofTotalSeconds(0))
            val completed =
                trackingDao.getAllCompletedPrayersBetweenTwoDates(startMillis = startDate,
                    endMillis = endDate)
            if (completed.filter { it.completed }.size == pastDays * 5) {
                badges.add(ConsecutivePrayerBadge(days = pastDays))
            }
        }

        return badges
    }
}

data class ConsecutivePrayerBadge(val days: Int)
