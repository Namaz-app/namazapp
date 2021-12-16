package ba.aadil.namaz

import ba.aadil.namaz.core.Result
import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.prayertimes.GetPrayerTimeForDate
import ba.aadil.namaz.tracking.TrackPrayerUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class TrackPrayerUseCaseTest {
    @Test
    fun testTrackCreatesDbEntry() {
        val fakeDao = FakeTrackingDao()
        val trackPrayerUseCase = TrackPrayerUseCase(fakeDao, object : GetPrayerTimeForDate {
            override suspend fun get(
                date: LocalDate,
                event: Events.Prayers
            ): Result<LocalDateTime> {
                return Result(null, null)
            }
        })

        runBlocking {
            val now = LocalDate.now()
            trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.AfterNoonPrayer, now)
            assertEquals(Events.Prayers.AfterNoonPrayer, fakeDao.startTrackingValue?.prayer)
            assertEquals(
                now.format(Track.dateFormatter),
                fakeDao.startTrackingValue?.date?.format(Track.dateFormatter)
            )
        }
    }

    @Test
    fun testPrayerTimeEntry() {
        val fakeDao = FakeTrackingDao()
        val fakeDate = LocalDateTime.now()
        val trackPrayerUseCase = TrackPrayerUseCase(fakeDao, object : GetPrayerTimeForDate {
            override suspend fun get(
                date: LocalDate,
                event: Events.Prayers
            ): Result<LocalDateTime> {
                return Result(fakeDate, null)
            }
        })

        runBlocking {
            val now = LocalDate.now()
            trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.MorningPrayer, now)
            assertEquals(
                fakeDao.startTrackingValue?.prayerDateTime, fakeDate.toEpochSecond(
                    ZoneOffset.ofTotalSeconds(0)
                )
            )
        }
    }

    class FakeTrackingDao : TrackingDao {
        var startTrackingValue: Track? = null
        var prayerForDayValue = mutableListOf<Track>()
        var togglePrayerValue: Events.Prayers? = null
        var toggleCompleted = false
        var toggleDate: String? = null

        override fun getPrayerForDay(prayer: Events.Prayers, date: String): List<Track> {
            return prayerForDayValue
        }

        override fun startTracking(track: Track) {
            startTrackingValue = track
        }

        override fun togglePrayerCompletion(
            prayer: Events.Prayers,
            completed: Boolean,
            date: String
        ) {
            togglePrayerValue = prayer
            toggleCompleted = completed
            toggleDate = date
        }
    }
}