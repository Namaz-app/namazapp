package ba.aadil.namaz.ui.tracking

import androidx.lifecycle.ViewModel
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.tracking.TrackPrayerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class TrackingViewModel(private val trackPrayerUseCase: TrackPrayerUseCase) : ViewModel() {
    suspend fun getTrackingFor(prayer: Events.Prayers): TrackingFragment.TrackingUIModel? {
        val track = withContext(Dispatchers.IO) {
            trackPrayerUseCase.getOrTrackPrayer(prayer, LocalDate.now())
        }

        return TrackingFragment.TrackingUIModel(prayer.toString(), track?.completed ?: false)
    }
}