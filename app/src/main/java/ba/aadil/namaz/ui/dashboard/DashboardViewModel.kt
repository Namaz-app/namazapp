package ba.aadil.namaz.ui.dashboard

import androidx.lifecycle.ViewModel
import ba.aadil.namaz.stats.GetStatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
    }

    fun updateToDate(newDate: LocalDate) {
        _toDate.value = newDate
    }

    fun getStatsBetweenSelectedDaysLive(): Flow<PrayingStatisticsStats> {
        return combine(
            _fromDate,
            _toDate,
            getStatisticsUseCase.getTrackingBetweenDaysLive(
                LocalDate.now(),
                LocalDate.now()
            )
        ) { from, to, _ ->
            Pair(
                getStatisticsUseCase.getStatsBetweenDays(from, to),
                getStatisticsUseCase.getStatsBetweenDays(from, to),
            )
        }.map { (selectedDates, today) ->
            PrayingStatisticsStats.Data(data = selectedDates,
                prayedTodayCount = today.trackedPrayers.count { it.completed })
        }.flowOn(Dispatchers.IO)
    }

    sealed class PrayingStatisticsStats {
        object Loading : PrayingStatisticsStats()
        data class Data(
            val data: GetStatisticsUseCase.PrayerStatistics,
            val prayedTodayCount: Int,
        ) : PrayingStatisticsStats()

        class Error(val error: String) : PrayingStatisticsStats()
    }
}