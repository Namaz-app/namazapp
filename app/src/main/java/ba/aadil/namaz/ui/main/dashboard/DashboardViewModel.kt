package ba.aadil.namaz.ui.main.dashboard

import ba.aadil.namaz.domain.usecase.GetCurrentUser
import ba.aadil.namaz.domain.usecase.GetEmojiAndCongratsForPrayedPrayers
import ba.aadil.namaz.domain.usecase.GetStatisticsUseCase
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class DashboardViewModel(
    private val getCurrentUser: GetCurrentUser,
    private val getStatisticsUseCase: GetStatisticsUseCase,
    private val getEmojiAndCongratsForPrayedPrayers: GetEmojiAndCongratsForPrayedPrayers,
) : androidx.lifecycle.ViewModel() {


    var userName = MutableStateFlow("")
        private set

    var todayDate = MutableStateFlow(LocalDate.now())
        private set

    init {
        userName.value = getCurrentUser.getName()
    }














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
        _toDate.value = when {
            newDate.isAfter(LocalDate.now()) -> LocalDate.now()
            newDate.isBefore(_fromDate.value) -> LocalDate.now()
            else -> newDate
        }
    }

    fun getStatsBetweenSelectedDaysLive(): Flow<PrayingStatisticsStats> {
        return combine(
            _fromDate,
            _toDate,
            getStatisticsUseCase.getTrackingBetweenDaysLive(
                LocalDate.now(),
                LocalDate.now()
            )
        ) { from, to, todayStats ->
            Pair(
                getStatisticsUseCase.getStatsBetweenDays(from, to),
                todayStats
            )
        }.map { (selectedDates, today) ->
            val prayedTodayCount = today.count { it.completed }
            val emoji = getEmojiAndCongratsForPrayedPrayers.get(prayedTodayCount)
            PrayingStatisticsStats.Data(data = selectedDates,
                prayedTodayCount = prayedTodayCount,
                congratsTextId = emoji.first,
                emoji = emoji.second,
                selectedDaysStats = SelectedDaysStats(selectedDates.trackedPrayers.size,
                    selectedDates.totalCount)
            )
        }.flowOn(Dispatchers.IO)
    }

    data class SelectedDaysStats(val prayedCount: Int, val totalPrayers: Int) : ViewModel

    sealed class PrayingStatisticsStats : ViewModel {
        object Loading : PrayingStatisticsStats()
        data class Data(
            val data: GetStatisticsUseCase.PrayerStatistics,
            val prayedTodayCount: Int,
            val emoji: String,
            val congratsTextId: Int,
            val selectedDaysStats: SelectedDaysStats,
        ) : PrayingStatisticsStats()

        class Error(val error: String) : PrayingStatisticsStats()
    }
}