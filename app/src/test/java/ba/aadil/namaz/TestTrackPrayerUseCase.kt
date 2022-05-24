package ba.aadil.namaz

import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.domain.PrayerEvent
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

class PrayerTrackingInfoPrayerUseCaseTest {
    @Test
    fun testTrackCreatesDbEntry() {
        val fakeDao = FakePrayerTrackingInfoDao()
        val trackPrayerUseCase = TrackPrayerUseCase(fakeDao, object : GetPrayerTimeForDate {
            override suspend fun get(
                date: LocalDate,
                event: PrayerEvent.Prayers,
            ): Result<LocalDateTime> {
                return Result(null, null)
            }
        })

        runBlocking {
            val now = LocalDate.now()
            trackPrayerUseCase.getOrTrackPrayer(PrayerEvent.Prayers.AfterNoonPrayer, now)
            assertEquals(PrayerEvent.Prayers.AfterNoonPrayer, fakeDao.startTrackingValue?.prayer)
            assertEquals(
                now.format(PrayerTrackingInfo.dateFormatter),
                fakeDao.startTrackingValue?.date?.format(PrayerTrackingInfo.dateFormatter)
            )
        }
    }

    @Test
    fun testPrayerTimeEntry() {
        val fakeDao = FakePrayerTrackingInfoDao()
        val fakeDate = LocalDateTime.now()
        val trackPrayerUseCase = TrackPrayerUseCase(fakeDao, object : GetPrayerTimeForDate {
            override suspend fun get(
                date: LocalDate,
                event: PrayerEvent.Prayers,
            ): Result<LocalDateTime> {
                return Result(fakeDate, null)
            }
        })

        runBlocking {
            val now = LocalDate.now()
            trackPrayerUseCase.getOrTrackPrayer(PrayerEvent.Prayers.MorningPrayer, now)
            assertEquals(
                fakeDao.startTrackingValue?.prayerDateTime, fakeDate.toEpochSecond(
                    ZoneOffset.ofTotalSeconds(0)
                )
            )
        }
    }

    @Test
    fun testMarkAsCompletedUpdatesDbAndPrayerTime() {
        val fakeDao = FakePrayerTrackingInfoDao()
        val fakeDate = LocalDateTime.now()
        val testTrack = PrayerTrackingInfo(
            0,
            PrayerEvent.Prayers.MorningPrayer,
            false,
            fakeDate.format(PrayerTrackingInfo.dateFormatter),
            0,
            0
        )
        fakeDao.prayerForDayValue = mutableListOf(testTrack)
        val trackPrayerUseCase = TrackPrayerUseCase(fakeDao, object : GetPrayerTimeForDate {
            override suspend fun get(
                date: LocalDate,
                event: PrayerEvent.Prayers,
            ): Result<LocalDateTime> {
                return Result(fakeDate, null)
            }
        })

        val today = LocalDate.now()

        runBlocking {
            val track = trackPrayerUseCase.getOrTrackPrayer(PrayerEvent.Prayers.MorningPrayer, today)
            assertEquals(false, track?.completed)

            val completedTime = LocalDateTime.now()
            trackPrayerUseCase.togglePrayed(
                PrayerEvent.Prayers.MorningPrayer,
                today.format(PrayerTrackingInfo.dateFormatter),
                completedTime,
                true
            )

            assertEquals(
                completedTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0)),
                fakeDao.completedTimeLong
            )
        }
    }

    class FakePrayerTrackingInfoDao : PrayerTrackingInfoDao {
        var startTrackingValue: PrayerTrackingInfo? = null
        var prayerForDayValue = mutableListOf<PrayerTrackingInfo>()
        var togglePrayerValue: PrayerEvent.Prayers? = null
        var toggleCompleted = false
        var completedTimeLong: Long = 0

        override fun getPrayerForDay(prayer: PrayerEvent.Prayers, date: String): List<PrayerTrackingInfo> {
            return prayerForDayValue
        }

        override fun insertPrayerTrackingInfo(prayerTrackingInfo: PrayerTrackingInfo) {
            startTrackingValue = prayerTrackingInfo
        }

        override fun togglePrayerCompletion(
            prayer: PrayerEvent.Prayers,
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
        ): List<PrayerTrackingInfo> {
            return listOf()
        }

        override fun getAllCompletedPrayersBetweenTwoDatesFlow(
            startEpoch: Long,
            endEpoch: Long,
        ): Flow<List<PrayerTrackingInfo>> {
            return flowOf()
        }
    }
}