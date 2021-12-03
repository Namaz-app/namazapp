package ba.aadil.namaz

import ba.aadil.namaz.db.CityOffset
import ba.aadil.namaz.db.OffsetDao
import ba.aadil.namaz.db.PrayerSchedule
import ba.aadil.namaz.db.PrayerScheduleDao
import ba.aadil.namaz.vaktija.PrayerRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PrayerTimesRepositoryTest {
    @Test
    fun addition_isCorrect() {
        val mockPrayerTimes = object : PrayerScheduleDao {
            override fun getPrayersForADay(date: String): List<PrayerSchedule> {
                return listOf(
                    PrayerSchedule(
                        1,
                        date = "01-01",
                        morningPrayer = "06:00",
                        sunrise = "07:00",
                        noonPrayer = "12:00",
                        afternoonPrayer = "14:00",
                        sunsetPrayer = "17:00",
                        nightPrayer = "18:30"
                    )
                )
            }
        }
        val mockOffset = object : OffsetDao {
            override fun getOffsetForACity(month: Int, locationId: Int): List<CityOffset> {
                return listOf(
                    CityOffset(
                        1,
                        1,
                        1,
                        0,
                        0,
                        0
                    )
                )
            }
        }

        val prayerTimes = PrayerRepository(mockPrayerTimes, mockOffset)
        runBlocking {
            val date = LocalDate.now()
            assertEquals("12:00", prayerTimes.timeForNoonPrayer(date, 1))
        }
    }
}