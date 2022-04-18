package ba.aadil.namaz.ui.main.tracking

import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.R
import ba.aadil.namaz.data.db.PrayerTrackingInfo
import ba.aadil.namaz.domain.PrayerEvents
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
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

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
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvents.MorningPrayer, now)
                val noonPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvents.NoonPrayer, now)
                val afternoonPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvents.AfterNoonPrayer, now)
                val sunsetPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvents.SunsetPrayer, now)
                val nightPrayer =
                    trackPrayerUseCase.getOrTrackPrayer(PrayerEvents.NightPrayer, now)

                val trackingModels = listOf(
                    TrackingFragment.TrackingUIModel(
                       R.string.morningPrayer,
                        morningPrayer?.completed ?: false,
                        PrayerEvents.MorningPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.noonPrayer,
                        noonPrayer?.completed ?: false,
                        PrayerEvents.NoonPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.afternoonPrayer,
                        afternoonPrayer?.completed ?: false,
                        PrayerEvents.AfterNoonPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.sunsetPrayer,
                        sunsetPrayer?.completed ?: false,
                        PrayerEvents.SunsetPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.nightPrayer,
                        nightPrayer?.completed ?: false,
                        PrayerEvents.NightPrayer
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

    fun markAsPrayed(prayer: PrayerEvents, completed: Boolean) {
        val now = LocalDateTime.now()
        val todayDate = LocalDate.now()
        viewModelScope.launch(Dispatchers.IO) {
            trackPrayerUseCase.togglePrayed(
                prayer,
                todayDate.toInstant(),
                now,
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