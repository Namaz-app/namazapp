package ba.aadil.namaz.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.stats.GetStatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class DashboardViewModel(private val getStatisticsUseCase: GetStatisticsUseCase) : ViewModel() {
    private val _dateStats =
        MutableStateFlow<PrayingStatisticsStats>(PrayingStatisticsStats.Loading)
    val dateStats: StateFlow<PrayingStatisticsStats> = _dateStats
    private val _fromDate = MutableStateFlow(LocalDate.now().minusDays(7))
    val fromDate = _fromDate.asStateFlow()
    private val _toDate = MutableStateFlow(LocalDate.now())
    val toDate = _toDate.asStateFlow()

    fun updateFromDate(newDate: LocalDate) {
        _fromDate.value = newDate
        getStatsBetweenSelectedDays()
    }

    fun updateToDate(newDate: LocalDate) {
        _toDate.value = newDate
        getStatsBetweenSelectedDays()
    }

    fun getStatsBetweenSelectedDays() {
        viewModelScope.launch {
            val stats = withContext(Dispatchers.IO) {
                getStatisticsUseCase.getStatsBetweenDays(fromDate.value, toDate.value)
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