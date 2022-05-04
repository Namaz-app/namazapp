package ba.aadil.namaz.ui.main.dashboard

import ba.aadil.namaz.R
import ba.aadil.namaz.data.db.model.PrayerTrackingInfo
import ba.aadil.namaz.data.db.dao.PrayerTrackingInfoDao
import ba.aadil.namaz.domain.usecase.UserRepository
import ba.aadil.namaz.util.toInstant
import kotlinx.coroutines.flow.*
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

class DashboardViewModel(
    private val userRepository: UserRepository,
    private val prayerTrackingInfoDao: PrayerTrackingInfoDao
) : androidx.lifecycle.ViewModel() {

    var userName = MutableStateFlow(userRepository.getName())
        private set

    var todayDate = MutableStateFlow(LocalDate.now())
        private set

    var prayedCount =
        getPrayerTrackingInfoForToday().map { prayers ->
            prayers.count { prayer ->
                prayer.isCompleted
            }
        }
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

    var selectedWeekStats: Flow<List<DailyPrayerCount>> =
        fromDate.combine(toDate) { fromDate, toDate ->
            prayerTrackingInfoDao.getCompletedPrayersBetweenTwoDatesFlow(
                fromDate.toInstant(),
                toDate.plusDays(1).toInstant()
            )
        }.flatMapLatest {
            it
        }.map { prayers ->
            prayers.groupBy { prayer ->
                val dayOfWeek = prayer.prayerDateTime.dayOfWeek
                dayOfWeek
            }.map { (dayOfTheWeek, prayers) ->
                DailyPrayerCount(
                    dayOfTheWeek,
                    prayers.count { prayer ->
                        prayer.isCompleted
                    }
                )
            }
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
        val endDatePlusDay = Instant.now().plus(1, ChronoUnit.DAYS)
        return prayerTrackingInfoDao.getCompletedPrayersBetweenTwoDatesFlow(
            Instant.now(),
            endDatePlusDay.toInstant()
        )
    }

    data class DailyPrayerCount(val dayOfWeek: DayOfWeek, val prayedCount: Int)
    data class EmojiData(val congratsTextId: Int, val emojiText: String)
}