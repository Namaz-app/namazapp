package ba.aadil.namaz

import ba.aadil.namaz.data.db.Badge
import ba.aadil.namaz.data.db.BadgesDao
import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.domain.PrayerEvents
import ba.aadil.namaz.domain.usecase.GetBadges
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestGetBadges {
    @Test
    fun shouldReturnABadge() {
        val mockTrackingDao = mockk<PrayerTrackingInfoDao>()
        val mockBadgesDao = mockk<BadgesDao>()
        every {
            mockTrackingDao.getAllCompletedPrayersBetweenTwoDates(any(),
                any())
        } returns listOf(1, 2, 3, 4, 5).map { getMockTrack(it) }
        every { mockBadgesDao.storeBadge(any()) } just runs

        val getBadges = GetBadges(mockTrackingDao, mockBadgesDao)

        runBlocking {
            getBadges.checkAndStoreBadges()
        }
        verify {
            mockBadgesDao.storeBadge(Badge(0, 1))
        }
    }

    fun getMockTrack(id: Int): PrayerTrackingInfo {
        return PrayerTrackingInfo(id,
            PrayerEvents.Prayers.MorningPrayer,
            true,
            "",
            0,
            0)
    }
}