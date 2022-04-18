package ba.aadil.namaz

import ba.aadil.namaz.data.db.PrayerTrackingInfo
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class TestGetStatisticsUseCase {
    @Test
    fun testStatisticsUseCaseForTwoDays() {
        val mockDao = mockk<PrayerTrackingInfoDao>()
        val prayedPrayers = mockk<PrayerTrackingInfo>()

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
        val mockDao = mockk<PrayerTrackingInfoDao>()
        val prayedPrayers = mockk<PrayerTrackingInfo>()

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