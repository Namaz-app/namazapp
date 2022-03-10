package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.data.db.CityOffset
import ba.aadil.namaz.data.db.OffsetDao
import ba.aadil.namaz.data.db.PrayerSchedule
import ba.aadil.namaz.data.db.PrayerScheduleDao
import ba.aadil.namaz.domain.Events
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PrayerSchedulesUseCase(
    private val getCurrentCityUseCase: GetCurrentCityUseCase,
    private val prayerScheduleDao: PrayerScheduleDao,
    private val offsetDao: OffsetDao
) {
    private val prayerTimeFormat = DateTimeFormatter.ofPattern("HH:mm")

    suspend fun getPrayerSchedule(date: LocalDate): EventsSchedule? {
        val locationId = getCurrentCityUseCase.getId()
        val monthWithLeadingZero = String.format(
            "%02d",
            date.month.value
        )
        val dayWithLeadingZero = String.format("%02d", date.dayOfMonth)
        val prayerSchedule =
            prayerScheduleDao.getPrayersForADay("$monthWithLeadingZero-$dayWithLeadingZero")
        val offset = offsetDao.getOffsetForACity(date.monthValue, locationId)

        prayerSchedule.firstOrNull()?.let { dailyPrayer ->
            offset.firstOrNull()?.let { offset ->
                LocalTime.parse(dailyPrayer.noonPrayer)
                return EventsSchedule(
                    morningPrayer = getFormattedTimeForEvents(
                        Events.Prayers.MorningPrayer,
                        dailyPrayer,
                        offset
                    ),
                    sunrise = getFormattedTimeForEvents(Events.Sunrise, dailyPrayer, offset),
                    noonPrayer = getFormattedTimeForEvents(
                        Events.Prayers.NoonPrayer,
                        dailyPrayer,
                        offset
                    ),
                    afterNoonPrayer = getFormattedTimeForEvents(
                        Events.Prayers.AfterNoonPrayer,
                        dailyPrayer,
                        offset
                    ),
                    sunsetPrayer = getFormattedTimeForEvents(
                        Events.Prayers.SunsetPrayer,
                        dailyPrayer,
                        offset
                    ),
                    nightPrayer = getFormattedTimeForEvents(
                        Events.Prayers.NightPrayer,
                        dailyPrayer,
                        offset
                    ),
                )
            }
        }

        return null
    }

    suspend fun getPrayerScheduleMap(date: LocalDate): Result<EventsMap> {
        val locationId = getCurrentCityUseCase.getId()
        val monthWithLeadingZero = String.format(
            "%02d",
            date.month.value
        )
        val dayWithLeadingZero = String.format("%02d", date.dayOfMonth)
        val prayerSchedule =
            prayerScheduleDao.getPrayersForADay("$monthWithLeadingZero-$dayWithLeadingZero")
        val offset = offsetDao.getOffsetForACity(date.monthValue, locationId)

        prayerSchedule.firstOrNull()?.let { dailyPrayer ->
            offset.firstOrNull()?.let { offset ->
                val eventsMap = hashMapOf<Events, String>()
                eventsMap[Events.Prayers.MorningPrayer] = getFormattedTimeForEvents(
                    Events.Prayers.MorningPrayer,
                    dailyPrayer,
                    offset
                )
                eventsMap[Events.Sunrise] = getFormattedTimeForEvents(
                    Events.Sunrise,
                    dailyPrayer,
                    offset
                )
                eventsMap[Events.Prayers.NoonPrayer] = getFormattedTimeForEvents(
                    Events.Prayers.NoonPrayer,
                    dailyPrayer,
                    offset
                )
                eventsMap[Events.Prayers.AfterNoonPrayer] = getFormattedTimeForEvents(
                    Events.Prayers.AfterNoonPrayer,
                    dailyPrayer,
                    offset
                )
                eventsMap[Events.Prayers.SunsetPrayer] = getFormattedTimeForEvents(
                    Events.Prayers.SunsetPrayer,
                    dailyPrayer,
                    offset
                )
                eventsMap[Events.Prayers.NightPrayer] = getFormattedTimeForEvents(
                    Events.Prayers.NightPrayer,
                    dailyPrayer,
                    offset
                )
                return Result.success(EventsMap(eventsMap))
            }
        }
        return Result.failure(Exception("Data can not be read from db"))
    }

    private fun getFormattedTimeForEvents(
        event: Events,
        dailyPrayer: PrayerSchedule,
        offset: CityOffset
    ): String {
        val morningPrayer = LocalTime.parse(dailyPrayer.morningPrayer, prayerTimeFormat)
            .plus(offset.morningPrayerOffset.toLong(), ChronoUnit.MINUTES)
        val sunrise = LocalTime.parse(dailyPrayer.sunrise, prayerTimeFormat)
            .plus(offset.morningPrayerOffset.toLong(), ChronoUnit.MINUTES)
        val noonPrayer = LocalTime.parse(dailyPrayer.noonPrayer, prayerTimeFormat)
            .plus(offset.noonPrayerOffset.toLong(), ChronoUnit.MINUTES)
        val afterNoonPrayer = LocalTime.parse(dailyPrayer.afternoonPrayer, prayerTimeFormat)
            .plus(offset.afternoonPrayerOffset.toLong(), ChronoUnit.MINUTES)
        val sunsetPrayer = LocalTime.parse(dailyPrayer.sunsetPrayer, prayerTimeFormat)
            .plus(offset.afternoonPrayerOffset.toLong(), ChronoUnit.MINUTES)
        val nightPrayer = LocalTime.parse(dailyPrayer.nightPrayer, prayerTimeFormat)
            .plus(offset.afternoonPrayerOffset.toLong(), ChronoUnit.MINUTES)

        return prayerTimeFormat.format(
            when (event) {
                Events.Prayers.MorningPrayer -> morningPrayer
                Events.Sunrise -> sunrise
                Events.Prayers.NoonPrayer -> noonPrayer
                Events.Prayers.AfterNoonPrayer -> afterNoonPrayer
                Events.Prayers.SunsetPrayer -> sunsetPrayer
                Events.Prayers.NightPrayer -> nightPrayer
            }
        )
    }

    data class EventsSchedule(
        val morningPrayer: String,
        val sunrise: String,
        val noonPrayer: String,
        val afterNoonPrayer: String,
        val sunsetPrayer: String,
        val nightPrayer: String
    )

    data class EventsMap(val map: Map<Events, String>)
}
