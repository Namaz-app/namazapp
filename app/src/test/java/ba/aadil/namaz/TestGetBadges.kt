package ba.aadil.namaz

import ba.aadil.namaz.db.Badge
import ba.aadil.namaz.db.BadgesDao
import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.user.GetBadges
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestGetBadges {
    @Test
    fun shouldReturnABadge() {
        val mockTrackingDao = mockk<TrackingDao>()
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

    fun getMockTrack(id: Int): Track {
        return Track(id,
            Events.Prayers.MorningPrayer,
            true,
            "",
            0,
            0)
    }
}