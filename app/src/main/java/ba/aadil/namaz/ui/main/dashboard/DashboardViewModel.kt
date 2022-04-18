package ba.aadil.namaz.ui.main.dashboard

import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.R
import ba.aadil.namaz.data.db.PrayerTrackingInfo
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.domain.PrayerEvents
import ba.aadil.namaz.domain.usecase.UserRepository
import ba.aadil.namaz.util.toInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime

class DashboardViewModel(
    private val userRepository: UserRepository,
    private val prayerTrackingInfoDao: PrayerTrackingInfoDao
) : androidx.lifecycle.ViewModel() {

    var userName = MutableStateFlow("")
        private set

    var todayDate = MutableStateFlow(LocalDate.now())
        private set

    var prayedCount =
        getPrayerTrackingInfoForToday().map { it.count { it.completed } }
        private set

    var emojiData = prayedCount.map {
        when (it) {
            0 -> EmojiData(
                R.string.congrats_gods_blessings3,
                "\uD83D\uDE1E"
            )
            1, 2 -> EmojiData(
                R.string.congrats_gods_blessings2,
                "\uD83D\uDE10"
            )
            3, 4 -> EmojiData(
                R.string.congrats_gods_blessings,
                "☺️"
            )
            else -> EmojiData(
                R.string.congrats_gods_blessings,
                "\uD83E\uDD29"
            )
        }
    }
        private set

    var fromDate = MutableStateFlow(LocalDate.now().minusDays(7))
        private set

    var toDate = MutableStateFlow(LocalDate.now())
        private set

    var selectedWeekStats: Flow<List<PrayerTrackingInfo>> =
        combine(fromDate, toDate) { fromDate, toDate ->
            val endDatePlusDay = toDate.plusDays(1)
            val prayed = prayerTrackingInfoDao.getAllPrayersBetweenTwoDates(
                fromDate.toInstant(),
                endDatePlusDay.toInstant()
            )
            prayed
        }.flowOn(Dispatchers.IO)

    init {
        userName.value = userRepository.getName()
    }

    fun updateToDate(newDate: LocalDate) {
        toDate.value = when {
            newDate.isAfter(LocalDate.now()) -> LocalDate.now()
            newDate.isBefore(fromDate.value) -> LocalDate.now()
            else -> newDate
        }
    }

    fun updateFromDate(newDate: LocalDate) {
        fromDate.value = newDate
    }

    private fun getPrayerTrackingInfoForToday(): Flow<List<PrayerTrackingInfo>> {
        val endDatePlusDay = LocalDate.now().plusDays(1)
        return prayerTrackingInfoDao.getAllPrayersBetweenTwoDatesFlow(
            Instant.now(),
            endDatePlusDay.toInstant()
        )
    }

    // private fun getStatsBetweenDays(startDate: LocalDate, endDate: LocalDate) {
    //     val endDatePlusDay = endDate.plusDays(1)
    //     val prayed = prayerTrackingInfoDao.getAllPrayersBetweenTwoDates(
    //         Instant.from(startDate),
    //         Instant.from(endDatePlusDay)
    //     )
    //     prayed
    //     val days =
    //         Duration.between(startDate.atStartOfDay(), endDatePlusDay.atStartOfDay()).toDays()
    //             .toInt()
    //     val totalPrayers = days * 5
    //
    //     prayed
    //     val completedMap = hashMapOf<PrayerEvents, Int>()
    //     prayed.filter { track -> track.completed }.forEach { track ->
    //         completedMap[track.prayer] = (completedMap[track.prayer] ?: 0) + 1
    //     }
    //
    //     listOf(
    //         PrayerEvents.MorningPrayer,
    //         PrayerEvents.NoonPrayer,
    //         PrayerEvents.AfterNoonPrayer,
    //         PrayerEvents.SunsetPrayer,
    //         PrayerEvents.NightPrayer,
    //     ).forEach {
    //         completedMap[it] = completedMap[it] ?: 0
    //     }
    //
    //     val prayerStats = completedMap.keys.map { key ->
    //         GetStatisticsUseCase.SinglePrayerStats(
    //             key,
    //             completedMap[key] ?: 0,
    //             days,
    //             key.sortWeight
    //         )
    //     }
    //     return PrayerStatistics(prayed, prayerStats, totalPrayers)
    // }

    data class DayWithPrayerCount(val dayOfWeek: DayOfWeek, val prayedCount: Int)
    data class EmojiData(val congratsTextId: Int, val emojiText: String)
}