package ba.aadil.namaz

import ba.aadil.namaz.domain.usecase.MockStoredCity
import ba.aadil.namaz.data.db.CityOffset
import ba.aadil.namaz.data.db.OffsetDao
import ba.aadil.namaz.data.db.PrayerSchedule
import ba.aadil.namaz.data.db.PrayerScheduleDao
import ba.aadil.namaz.domain.usecase.PrayerSchedulesUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PrayerSchedulesUseCaseTest {
    @Test
    fun testNoonPrayerTime() {
        val mockOffset = getOffsetObject(0)
        val prayerTimes =
            PrayerSchedulesUseCase(MockStoredCity(), getPrayerTimes(), mockOffset)

        runBlocking {
            val date = LocalDate.now()
            assertEquals("12:00", prayerTimes.getPrayerSchedule(date)?.noonPrayer)
        }
    }

    @Test
    fun testNoonPrayerOffsetIsAdded() {
        val mockOffset = getOffsetObject(10)
        val prayerTimes = PrayerSchedulesUseCase(MockStoredCity(), getPrayerTimes(), mockOffset)

        runBlocking {
            val date = LocalDate.now()
            assertEquals("12:10", prayerTimes.getPrayerSchedule(date)?.noonPrayer)
        }
    }

    @Test
    fun testMorningPrayerTime() {
        val mockOffset = getOffsetObject(0)
        val prayerTimes = PrayerSchedulesUseCase(MockStoredCity(), getPrayerTimes(), mockOffset)

        runBlocking {
            val date = LocalDate.now()
            assertEquals("06:00", prayerTimes.getPrayerSchedule(date)?.morningPrayer)
        }
    }

    private fun getOffsetObject(noonOffset: Int): OffsetDao {
        return object : OffsetDao {
            override fun getOffsetForACity(month: Int, locationId: Int): List<CityOffset> {
                return listOf(
                    CityOffset(
                        1,
                        1,
                        1,
                        0,
                        noonOffset,
                        0
                    )
                )
            }
        }
    }

    private fun getPrayerTimes(): PrayerScheduleDao {
        return object : PrayerScheduleDao {
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
    }
}