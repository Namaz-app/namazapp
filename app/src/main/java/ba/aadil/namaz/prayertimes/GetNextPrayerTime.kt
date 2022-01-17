package ba.aadil.namaz.prayertimes

import ba.aadil.namaz.db.Track.Companion.timeFormatterNoSeconds
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GetNextPrayerTime(private val prayerSchedulesUseCase: PrayerSchedulesUseCase) {
    suspend fun get(now: LocalDateTime = LocalDateTime.now()): Pair<LocalDateTime, Events.Prayers> {
        val eventsSchedule = prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now())
        val eventsScheduleTomorrow =
            prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now().plusDays(1))
        val prayerTimes = mutableListOf<Pair<LocalDateTime, Events.Prayers>>()
        eventsScheduleTomorrow?.let {
            prayerTimes.add(Pair(LocalTime.parse(it.morningPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now().plusDays(1)),
                Events.Prayers.MorningPrayer))
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

        return prayerTimes.filter { it.first.isAfter(now) || it.first.isEqual(now) }.map {
            Pair(Duration.between(now, it.first), it)
        }.minByOrNull { it.first }?.second ?: Pair(now,
            Events.Prayers.MorningPrayer)
    }
}