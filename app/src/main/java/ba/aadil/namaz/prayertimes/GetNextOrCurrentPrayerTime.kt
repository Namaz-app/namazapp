package ba.aadil.namaz.prayertimes

import ba.aadil.namaz.db.Track.Companion.timeFormatterNoSeconds
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GetNextOrCurrentPrayerTime(private val prayerSchedulesUseCase: PrayerSchedulesUseCase) {
    suspend fun getNext(now: LocalDateTime = LocalDateTime.now()): Pair<LocalDateTime, Events.Prayers> {
        val prayerTimes = getPrayerList()

        return prayerTimes.filter { it.first.isAfter(now) || it.first.isEqual(now) }.map {
            Pair(Duration.between(now, it.first), it)
        }.minByOrNull { it.first }?.second ?: Pair(now,
            Events.Prayers.MorningPrayer)
    }

    suspend fun getCurrent(now: LocalDateTime = LocalDateTime.now()): Pair<LocalDateTime, Events.Prayers> {
        val prayerTimes = getPrayerList()
        return prayerTimes.filter { it.first.isBefore(now) || it.first.isEqual(now) }.map {
            Pair(Duration.between(it.first, now), it)
        }.minByOrNull { it.first }?.second ?: Pair(now,
            Events.Prayers.MorningPrayer)
    }

    private suspend fun getPrayerList(): MutableList<Pair<LocalDateTime, Events.Prayers>> {
        val prayerTimes = mutableListOf<Pair<LocalDateTime, Events.Prayers>>()
        val eventsSchedule = prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now())
        val eventsScheduleTomorrow =
            prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now().plusDays(1))
        val eventsScheduleYesterday =
            prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now().minusDays(1))
        eventsScheduleTomorrow?.let {
            prayerTimes.add(Pair(LocalTime.parse(it.morningPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now().plusDays(1)),
                Events.Prayers.MorningPrayer))
        }
        eventsScheduleYesterday?.let {
            prayerTimes.add(Pair(LocalTime.parse(it.nightPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now().minusDays(1)),
                Events.Prayers.NightPrayer))
        }
        eventsSchedule?.let {
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.morningPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now()),
                Events.Prayers.MorningPrayer))
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.noonPrayer, timeFormatterNoSeconds)
                .atDate(LocalDate.now()),
                Events.Prayers.NoonPrayer))
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.afterNoonPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now()),
                Events.Prayers.AfterNoonPrayer))
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.sunsetPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now()),
                Events.Prayers.SunsetPrayer))
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.nightPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now()),
                Events.Prayers.NightPrayer))
        }

        return prayerTimes
    }
}