package ba.aadil.namaz.ui.main.tracking

import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.R
import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.domain.PrayerEvent
import ba.aadil.namaz.domain.usecase.TrackPrayerUseCase
import ba.aadil.namaz.domain.usecase.GetBadges
import ba.aadil.namaz.domain.usecase.UserRepository
import ba.aadil.namaz.util.toInstant
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

class TrackingViewModel(
    private val trackPrayerUseCase: TrackPrayerUseCase,
    private val getBadges: GetBadges,
    private val userRepository: UserRepository,
) : androidx.lifecycle.ViewModel() {
    private val _state = MutableStateFlow<TrackingPrayersState>(TrackingPrayersState.Loading)
    val state: StateFlow<TrackingPrayersState> = _state

    fun getTracking() {
        viewModelScope.launch {
            _state.value = withContext(Dispatchers.IO) {
                val now = LocalDate.now()
                val morningPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvent.MorningPrayer, now)
                val noonPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvent.NoonPrayer, now)
                val afternoonPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvent.AfterNoonPrayer, now)
                val sunsetPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvent.SunsetPrayer, now)
                val nightPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvent.NightPrayer, now)

                val trackingModels = listOf(
                    TrackingFragment.TrackingUIModel(
                       R.string.morningPrayer,
                        morningPrayer?.completed ?: false,
                        PrayerEvent.MorningPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.noonPrayer,
                        noonPrayer?.completed ?: false,
                        PrayerEvent.NoonPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.afternoonPrayer,
                        afternoonPrayer?.completed ?: false,
                        PrayerEvent.AfterNoonPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.sunsetPrayer,
                        sunsetPrayer?.completed ?: false,
                        PrayerEvent.SunsetPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.nightPrayer,
                        nightPrayer?.completed ?: false,
                        PrayerEvent.NightPrayer
                    ),
                )
                TrackingPrayersState.Data(
                    listOf(
                        TrackingFragment.TrackingHeader(userRepository.getName(),
                            trackingModels.count { it.track },
                            PrayerTrackingInfo.longDateFormatter.format(LocalDate.now())),
                    ) + trackingModels
                )
            }
        }
    }

    fun markAsPrayed(prayer: PrayerEvent, completed: Boolean) {
        val now = LocalDateTime.now()
        val todayDate = LocalDate.now()
        viewModelScope.launch(Dispatchers.IO) {
            trackPrayerUseCase.togglePrayed(
                prayer,
                todayDate.toInstant(),
                completed
            )
            getBadges.checkAndStoreBadges()
        }
        getTracking()
    }

    sealed class TrackingPrayersState {
        object Loading : TrackingPrayersState()
        data class Data(val list: List<ViewModel>) : TrackingPrayersState()
        class Error(val error: String) : TrackingPrayersState()
    }
}