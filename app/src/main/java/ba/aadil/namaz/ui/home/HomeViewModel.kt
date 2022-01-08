package ba.aadil.namaz.ui.home

import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.city.GetCurrentDateTimeAndCity
import ba.aadil.namaz.prayertimes.PrayerSchedulesUseCase
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val prayerSchedulesUseCase: PrayerSchedulesUseCase,
    private val getCurrentDateTimeAndCity: GetCurrentDateTimeAndCity,
) : androidx.lifecycle.ViewModel() {
    private val _data = MutableStateFlow<VaktijaUIModel?>(null)
    val data = _data.asStateFlow()
    private val _dateTimeCity = MutableStateFlow<GetCurrentDateTimeAndCity.Data?>(null)
    val dateTimeCity = _dateTimeCity.asStateFlow()

    init {
        viewModelScope.launch {
            _dateTimeCity.value = getCurrentDateTimeAndCity.get()
            delay(TimeUnit.SECONDS.toMillis(1))
        }
    }

    fun getPrayersSchedule() {
        viewModelScope.launch {
            while (true) {
                _data.value = withContext(Dispatchers.IO) {
                    val prayerScheduleData =
                        prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now())
                    prayerScheduleData?.let {
                        VaktijaUIModel(prayerScheduleData,
                            getCurrentDateTimeAndCity.get())
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
) : ViewModel
