package ba.aadil.namaz

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.stats.GetStatisticsUseCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class TestGetStatisticsUseCase {
    @Test
    fun testStatisticsUseCaseForTwoDays() {
        val mockDao = mockk<TrackingDao>()
        val prayedPrayers = mockk<Track>()

        every { mockDao.getAllCompletedPrayersBetweenTwoDates(any(), any()) } returns listOf(
            prayedPrayers
        )

        val getStatisticsUseCase = GetStatisticsUseCase(mockDao)
        val today = LocalDate.now()
        val twoDaysAgo = today.minusDays(2)
        val stats = getStatisticsUseCase.getStatsBetweenDays(twoDaysAgo, today)
        assertEquals(10, stats.totalCount)
        assertEquals(1, stats.trackedPrayers)
    }

    @Test
    fun testStatisticsUseCaseForSevenDays() {
        val mockDao = mockk<TrackingDao>()
        val prayedPrayers = mockk<Track>()

        every { mockDao.getAllCompletedPrayersBetweenTwoDates(any(), any()) } returns listOf(
            prayedPrayers
        )

        val getStatisticsUseCase = GetStatisticsUseCase(mockDao)
        val today = LocalDate.now()
        val days = 7
        val startDate = today.minusDays(days.toLong())
        val stats = getStatisticsUseCase.getStatsBetweenDays(startDate, today)
        assertEquals(days * 5, stats.totalCount)
        assertEquals(1, stats.trackedPrayers)
    }
}