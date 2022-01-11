package ba.aadil.namaz.ui.home

import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.city.GetCurrentDateTimeAndCity
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.prayertimes.GetNextPrayerTime
import ba.aadil.namaz.prayertimes.PrayerSchedulesUseCase
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val prayerSchedulesUseCase: PrayerSchedulesUseCase,
    private val getCurrentDateTimeAndCity: GetCurrentDateTimeAndCity,
    private val getNextPrayerTime: GetNextPrayerTime,
) : androidx.lifecycle.ViewModel() {
    private val _data = MutableStateFlow<VaktijaUIModel?>(null)
    val data = _data.asStateFlow()
    private val _dateTimeCity = MutableStateFlow<GetCurrentDateTimeAndCity.Data?>(null)
    private val _untilNextPrayer =
        MutableStateFlow<Pair<LocalDateTime, Events.Prayers>>(Pair(LocalDateTime.now(),
            Events.Prayers.MorningPrayer))
    val dateTimeCity = _dateTimeCity.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _untilNextPrayer.value = getNextPrayerTime.get()
            }
        }
        viewModelScope.launch {
            if (_untilNextPrayer.value.first.isAfter(LocalDateTime.now())) {
                _untilNextPrayer.value = getNextPrayerTime.get()
            }
            _dateTimeCity.value = getCurrentDateTimeAndCity.get()
            delay(TimeUnit.SECONDS.toMillis(1))
        }
    }

    private fun getTillNextPrayer(): String {
        val durationBetween =
            Duration.between(LocalDateTime.now(), _untilNextPrayer.value.first)
        val durationHours = durationBetween.toHours()
        val durationMinutes = durationBetween.minusHours(durationHours).toMinutes()
        val durationSeconds =
            durationBetween.minusHours(durationHours).minusMinutes(durationMinutes).seconds
        return "$durationHours:$durationMinutes:$durationSeconds"
    }

    fun getPrayersSchedule() {
        viewModelScope.launch {
            while (true) {
                _data.value = withContext(Dispatchers.IO) {
                    val prayerScheduleData =
                        prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now())
                    prayerScheduleData?.let {
                        VaktijaUIModel(
                            prayerScheduleData,
                            getCurrentDateTimeAndCity.get(),
                            getTillNextPrayer()
                        )
                    }

                }
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
    }

}

data class VaktijaUIModel(
    val prayerSchedulesUseCase: PrayerSchedulesUseCase.EventsSchedule,
    val getCurrentDateTimeAndCity: GetCurrentDateTimeAndCity.Data,
    val remainingTimeTillNextPrayer: String,
) : ViewModel
