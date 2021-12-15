package ba.aadil.namaz.prayertimes

import ba.aadil.namaz.city.GetCurrentCityUseCase
import ba.aadil.namaz.db.CityOffset
import ba.aadil.namaz.db.OffsetDao
import ba.aadil.namaz.db.PrayerSchedule
import ba.aadil.namaz.db.PrayerScheduleDao
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
}
