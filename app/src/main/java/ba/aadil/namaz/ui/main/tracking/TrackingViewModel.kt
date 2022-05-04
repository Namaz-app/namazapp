package ba.aadil.namaz.ui.main.tracking

import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.R
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.domain.PrayerEvents
import ba.aadil.namaz.domain.usecase.GetBadges
import ba.aadil.namaz.domain.usecase.TrackPrayerUseCase
import ba.aadil.namaz.domain.usecase.UserRepository
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneOffset

class TrackingViewModel(
    private val getBadges: GetBadges,
    private val userRepository: UserRepository,
    private val prayerTrackingInfoDao: PrayerTrackingInfoDao
) : androidx.lifecycle.ViewModel() {
    private val _state = MutableStateFlow<TrackingPrayersState>(TrackingPrayersState.Loading)
    val state: StateFlow<TrackingPrayersState> = _state

    fun getTracking() {
        viewModelScope.launch {
            _state.value = withContext(Dispatchers.IO) {
                val now = LocalDate.now()
                val trackingModels = listOf(
                    TrackingFragment.TrackingUIModel(
                       R.string.morningPrayer,
                          false,
                        PrayerEvents.MorningPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.noonPrayer,
                        false,
                        PrayerEvents.NoonPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.afternoonPrayer,
                         false,
                        PrayerEvents.AfterNoonPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.sunsetPrayer,
                         false,
                        PrayerEvents.SunsetPrayer
                    ),
                    TrackingFragment.TrackingUIModel(
                       R.string.nightPrayer,
                         false,
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

    // fun markAsPrayed(prayer: PrayerEvents, completed: Boolean) {
    //     viewModelScope.launch {
    //         prayerTrackingInfoDao.insertPrayerTrackingInfo(
    //             PrayerTrackingInfo(
    //                 prayerDateTime = prayerDate,
    //                 prayer = prayer,
    //                 isCompleted = completed,
    //                 completedDateTime = time.toInstant(ZoneOffset.UTC)
    //             )
    //         )
    //         getBadges.checkAndStoreBadges()
    //     }
    // }

    sealed class TrackingPrayersState {
        object Loading : TrackingPrayersState()
        data class Data(val list: List<ViewModel>) : TrackingPrayersState()
        class Error(val error: String) : TrackingPrayersState()
    }
}