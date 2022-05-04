package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.data.db.Badge
import ba.aadil.namaz.data.db.BadgesDao
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.util.toInstant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class GetBadges(val prayerTrackingInfoDao: PrayerTrackingInfoDao, val badgesDao: BadgesDao) {
    suspend fun checkAndStoreBadges(): List<ConsecutivePrayerBadge> {
        val badges = mutableListOf<ConsecutivePrayerBadge>()
        listOf(1, 7, 14, 21, 28, 35, 42).forEach { pastDays ->
            val endDate = LocalDateTime.now().toInstant(ZoneOffset.UTC)
            val startDate = LocalDate.now().minusDays(pastDays.toLong()).toInstant()
            val completed =
                prayerTrackingInfoDao.getAllPrayersBetweenTwoDates(startDate, endDate)
            if (completed.filter { it.isCompleted }.size == pastDays * 5) {
                badges.add(ConsecutivePrayerBadge(days = pastDays))
            }
        }
        badges.forEach { badgesDao.storeBadge(Badge(id = 0, completedDays = it.days)) }

        return badges
    }
}

data class ConsecutivePrayerBadge(val days: Int)
