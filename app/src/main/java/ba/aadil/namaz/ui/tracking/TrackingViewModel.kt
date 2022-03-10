package ba.aadil.namaz.ui.tracking

import android.content.Context
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.R
import ba.aadil.namaz.data.db.Track
import ba.aadil.namaz.domain.Events
import ba.aadil.namaz.domain.usecase.TrackPrayerUseCase
import ba.aadil.namaz.domain.usecase.GetBadges
import ba.aadil.namaz.domain.usecase.GetCurrentUser
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

class TrackingViewModel(
    private val context: Context,
    private val trackPrayerUseCase: TrackPrayerUseCase,
    private val getBadges: GetBadges,
    private val getCurrentUser: GetCurrentUser,
) : androidx.lifecycle.ViewModel() {
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

                val trackingModels = listOf(
                    TrackingFragment.TrackingUIModel(
                        context.getString(R.string.morningPrayer),
                        morningPrayer?.completed ?: false,
                        Events.Prayers.MorningPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                        context.getString(R.string.noonPrayer),
                        noonPrayer?.completed ?: false,
                        Events.Prayers.NoonPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                        context.getString(R.string.afternoonPrayer),
                        afternoonPrayer?.completed ?: false,
                        Events.Prayers.AfterNoonPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                        context.getString(R.string.sunsetPrayer),
                        sunsetPrayer?.completed ?: false,
                        Events.Prayers.SunsetPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                        context.getString(R.string.nightPrayer),
                        nightPrayer?.completed ?: false,
                        Events.Prayers.NightPrayer
                    ),
                )
                TrackingPrayersState.Data(
                    listOf(
                        TrackingFragment.TrackingHeader(getCurrentUser.getName(),
                            trackingModels.count { it.track },
                            Track.longDateFormatter.format(LocalDate.now())),
                    ) + trackingModels
                )
            }
        }
    }

    fun markAsPrayed(prayer: Events.Prayers, completed: Boolean) {
        val now = LocalDateTime.now()
        val todayDate = LocalDate.now()
        viewModelScope.launch(Dispatchers.IO) {
            trackPrayerUseCase.togglePrayed(
                prayer,
                Track.dateFormatter.format(todayDate),
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