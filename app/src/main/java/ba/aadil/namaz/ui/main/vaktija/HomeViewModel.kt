package ba.aadil.namaz.ui.main.vaktija

import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.domain.usecase.GetCurrentDateTimeAndCity
import ba.aadil.namaz.domain.PrayerEvents
import ba.aadil.namaz.domain.usecase.GetNextOrCurrentPrayerTime
import ba.aadil.namaz.domain.usecase.PrayerSchedulesUseCase
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
    private val getNextOrCurrentPrayerTime: GetNextOrCurrentPrayerTime,
) : androidx.lifecycle.ViewModel() {
    private val _data = MutableStateFlow<VaktijaUIModel?>(null)
    val data = _data.asStateFlow()
    private val _dateTimeCity = MutableStateFlow<GetCurrentDateTimeAndCity.Data?>(null)
    private val _untilNextPrayer =
        MutableStateFlow<Pair<LocalDateTime, PrayerEvents>>(Pair(LocalDateTime.now(),
            PrayerEvents.MorningPrayer))

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _untilNextPrayer.value = getNextOrCurrentPrayerTime.getNext()
            }
        }
    }

    fun getPrayersSchedule() {
        viewModelScope.launch {
            while (true) {
                withContext(Dispatchers.IO) {
                    if (_untilNextPrayer.value.first.isAfter(LocalDateTime.now())) {
                        _untilNextPrayer.value = getNextOrCurrentPrayerTime.getNext()
                    }
                    _dateTimeCity.value = getCurrentDateTimeAndCity.get()
                }
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

    private fun getTillNextPrayer(): String {
        val durationBetween =
            Duration.between(LocalDateTime.now(), _untilNextPrayer.value.first)
        val durationHours = durationBetween.toHours()
        val durationMinutes = durationBetween.minusHours(durationHours).toMinutes()
        val durationSeconds =
            durationBetween.minusHours(durationHours).minusMinutes(durationMinutes).seconds
        return "${String.format("%02d", durationHours)}:${
            String.format("%02d",
                durationMinutes)
        }:${String.format("%02d", durationSeconds)}"
    }
}

data class VaktijaUIModel(
    val prayerSchedulesUseCase: PrayerSchedulesUseCase.EventsSchedule,
    val getCurrentDateTimeAndCity: GetCurrentDateTimeAndCity.Data,
    val remainingTimeTillNextPrayer: String,
) : ViewModel
