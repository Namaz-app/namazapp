package ba.aadil.namaz.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.stats.GetStatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class DashboardViewModel(private val getStatisticsUseCase: GetStatisticsUseCase) : ViewModel() {
    private val _dateStats =
        MutableStateFlow<PrayingStatisticsStats>(PrayingStatisticsStats.Loading)
    val dateStats: StateFlow<PrayingStatisticsStats> = _dateStats

    fun getStatsBetweenDays(startDay: LocalDate, endDay: LocalDate) {
        viewModelScope.launch {
            val stats = withContext(Dispatchers.IO) {
                getStatisticsUseCase.getStatsBetweenDays(startDay, endDay)
            }
            _dateStats.value = PrayingStatisticsStats.Data(stats)
        }
    }

    sealed class PrayingStatisticsStats {
        object Loading : PrayingStatisticsStats()
        data class Data(val stats: GetStatisticsUseCase.PrayerStatistics) : PrayingStatisticsStats()
        class Error(val error: String) : PrayingStatisticsStats()
    }
}