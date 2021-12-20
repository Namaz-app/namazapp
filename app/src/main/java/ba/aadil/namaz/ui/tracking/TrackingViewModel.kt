package ba.aadil.namaz.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.tracking.TrackPrayerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class TrackingViewModel(private val trackPrayerUseCase: TrackPrayerUseCase) : ViewModel() {
    private val _state = MutableStateFlow<TrackingPrayersState>(TrackingPrayersState.Loading)
    val state: StateFlow<TrackingPrayersState> = _state

    fun getTracking() {
        viewModelScope.launch {
            _state.value = withContext(Dispatchers.IO) {
                val now = LocalDate.now()
                val morningPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.MorningPrayer, now)
                val noonPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.NoonPrayer, now)
                val afternoonPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.AfterNoonPrayer, now)
                val sunsetPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.SunsetPrayer, now)
                val nightPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(Events.Prayers.NightPrayer, now)

                TrackingPrayersState.Data(
                    listOf(
                        TrackingFragment.TrackingUIModel(
                            Events.Prayers.MorningPrayer.toString(),
                            morningPrayer?.completed ?: false
                        ),
                        TrackingFragment.TrackingUIModel(
                            Events.Prayers.NoonPrayer.toString(),
                            noonPrayer?.completed ?: false
                        ),
                        TrackingFragment.TrackingUIModel(
                            Events.Prayers.AfterNoonPrayer.toString(),
                            afternoonPrayer?.completed ?: false
                        ),
                        TrackingFragment.TrackingUIModel(
                            Events.Prayers.SunsetPrayer.toString(),
                            sunsetPrayer?.completed ?: false
                        ),
                        TrackingFragment.TrackingUIModel(
                            Events.Prayers.NightPrayer.toString(),
                            nightPrayer?.completed ?: false
                        ),
                    )
                )
            }
        }
    }

    sealed class TrackingPrayersState {
        object Loading : TrackingPrayersState()
        data class Data(val list: List<TrackingFragment.TrackingUIModel>) : TrackingPrayersState()
        class Error(val error: String) : TrackingPrayersState()
    }
}