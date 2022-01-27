package ba.aadil.namaz

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.user.GetBadges
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Test

class TestGetBadges {
    @Test
    fun shouldReturnABadge() {
        val mockTrackingDao = mockk<TrackingDao>()
        every {
            mockTrackingDao.getAllCompletedPrayersBetweenTwoDates(any(),
                any())
        } returns listOf(1, 2, 3, 4, 5).map { getMockTrack(it) }

        val getBadges = GetBadges(mockTrackingDao)

        assertEquals(1, getBadges.getAllBadges().size)
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