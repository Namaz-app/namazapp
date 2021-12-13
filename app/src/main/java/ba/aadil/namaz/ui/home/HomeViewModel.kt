package ba.aadil.namaz.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.prayertimes.PrayerSchedulesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HomeViewModel(
    private val prayerSchedulesUseCase: PrayerSchedulesUseCase,
) : ViewModel() {
    private val _prayerSchedule = MutableStateFlow<PrayerSchedulesUseCase.EventsSchedule?>(null)
    val prayersSchedule = _prayerSchedule.asStateFlow()

    fun getPrayersSchedule() {
        viewModelScope.launch {
            _prayerSchedule.value = withContext(Dispatchers.IO) {
                prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now())
            }
        }
    }
}