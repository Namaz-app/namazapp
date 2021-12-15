package ba.aadil.namaz

import ba.aadil.namaz.db.Track
import ba.aadil.namaz.db.TrackingDao
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.tracking.TrackPrayerUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDate

class TrackPrayerUseCaseTest {
    @Test
    fun testTrackCreatesDbEntry() {
        val trackPrayerUseCase = TrackPrayerUseCase(object : TrackingDao {
            override fun getPrayerForDay(prayer: Events.Prayers, date: String): List<Track> {
                return listOf()
            }

            override fun startTracking(track: Track) {
            }

            override fun togglePrayerCompletion(
                prayer: Events.Prayers,
                completed: Boolean,
                date: String
            ) {
            }
        })

        runBlocking {
            trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.AfterNoonPrayer, LocalDate.now())
        }
    }
}