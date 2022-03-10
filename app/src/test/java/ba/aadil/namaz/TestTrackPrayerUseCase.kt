package ba.aadil.namaz

import ba.aadil.namaz.core.Result
import ba.aadil.namaz.data.db.Track
import ba.aadil.namaz.data.db.TrackingDao
import ba.aadil.namaz.domain.Events
import ba.aadil.namaz.domain.usecase.GetPrayerTimeForDate
import ba.aadil.namaz.domain.usecase.TrackPrayerUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
                event: Events.Prayers,
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
                event: Events.Prayers,
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

    @Test
    fun testMarkAsCompletedUpdatesDbAndPrayerTime() {
        val fakeDao = FakeTrackingDao()
        val fakeDate = LocalDateTime.now()
        val testTrack = Track(
            0,
            Events.Prayers.MorningPrayer,
            false,
            fakeDate.format(Track.dateFormatter),
            0,
            0
        )
        fakeDao.prayerForDayValue = mutableListOf(testTrack)
        val trackPrayerUseCase = TrackPrayerUseCase(fakeDao, object : GetPrayerTimeForDate {
            override suspend fun get(
                date: LocalDate,
                event: Events.Prayers,
            ): Result<LocalDateTime> {
                return Result(fakeDate, null)
            }
        })

        val today = LocalDate.now()

        runBlocking {
            val track = trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.MorningPrayer, today)
            assertEquals(false, track?.completed)

            val completedTime = LocalDateTime.now()
            trackPrayerUseCase.togglePrayed(
                Events.Prayers.MorningPrayer,
                today.format(Track.dateFormatter),
                completedTime,
                true
            )

            assertEquals(
                completedTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0)),
                fakeDao.completedTimeLong
            )
        }
    }

    class FakeTrackingDao : TrackingDao {
        var startTrackingValue: Track? = null
        var prayerForDayValue = mutableListOf<Track>()
        var togglePrayerValue: Events.Prayers? = null
        var toggleCompleted = false
        var completedTimeLong: Long = 0

        override fun getPrayerForDay(prayer: Events.Prayers, date: String): List<Track> {
            return prayerForDayValue
        }

        override fun startTracking(track: Track) {
            startTrackingValue = track
        }

        override fun togglePrayerCompletion(
            prayer: Events.Prayers,
            date: String,
            completed: Boolean,
            completedTime: Long,
        ) {
            togglePrayerValue = prayer
            toggleCompleted = completed
            completedTimeLong = completedTime
        }

        override fun getAllCompletedPrayersBetweenTwoDates(
            startMillis: Long,
            endMillis: Long,
        ): List<Track> {
            return listOf()
        }

        override fun getAllCompletedPrayersBetweenTwoDatesFlow(
            startEpoch: Long,
            endEpoch: Long,
        ): Flow<List<Track>> {
            return flowOf()
        }
    }
}