package ba.aadil.namaz

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.tracking.TrackPrayerUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDate

class TrackPrayerUseCaseTest {
    @Test
    fun testTrackCreatesDbEntry() {
        val fakeDao = FakeTrackingDao()
        val trackPrayerUseCase = TrackPrayerUseCase(fakeDao)

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