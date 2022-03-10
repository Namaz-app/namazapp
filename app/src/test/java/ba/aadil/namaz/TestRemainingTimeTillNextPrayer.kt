package ba.aadil.namaz

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.prayertimes.GetNextOrCurrentPrayerTime
import ba.aadil.namaz.prayertimes.PrayerSchedulesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class TestRemainingTimeTillNextPrayer {

    @Test
    fun testNextPrayerTime() {
        val prayerSchedules: PrayerSchedulesUseCase = mockk()
        val nextPrayerTime = GetNextOrCurrentPrayerTime(prayerSchedules)
        val today = LocalDate.now()
        val tomorrow = LocalDate.now().plusDays(1)
        coEvery { prayerSchedules.getPrayerSchedule(today) } returns
                PrayerSchedulesUseCase.EventsSchedule("05:05",
                    "07:00",
                    "12:00",
                    "14:00",
                    "17:00",
                    "18:00")
        coEvery { prayerSchedules.getPrayerSchedule(tomorrow) } returns
                PrayerSchedulesUseCase.EventsSchedule(
                    "05:05",
                    "07:00",
                    "12:00",
                    "14:00",
                    "17:00",
                    "18:00")


        val now = LocalDateTime.now().withHour(10)
        runBlocking {
            val (time, nextPrayer) = nextPrayerTime.getNext(now)
            assertEquals("12:00:00", time.format(Track.timeFormatter))
            assertEquals(Events.Prayers.NoonPrayer, nextPrayer)
        }
    }
}