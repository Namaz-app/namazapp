package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.data.db.model.PrayerTrackingInfo.Companion.timeFormatterNoSeconds
import ba.aadil.namaz.domain.PrayerEvent
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GetNextOrCurrentPrayerTime(private val prayerSchedulesUseCase: PrayerSchedulesUseCase) {
    suspend fun getNext(now: LocalDateTime = LocalDateTime.now()): Pair<LocalDateTime, PrayerEvent> {
        val prayerTimes = getPrayerList()

        return prayerTimes.filter { it.first.isAfter(now) || it.first.isEqual(now) }.map {
            Pair(Duration.between(now, it.first), it)
        }.minByOrNull { it.first }?.second ?: Pair(now,
            PrayerEvent.MorningPrayer
        )
    }

    suspend fun getCurrent(now: LocalDateTime = LocalDateTime.now()): Pair<LocalDateTime, PrayerEvent> {
        val prayerTimes = getPrayerList()
        return prayerTimes.filter { it.first.isBefore(now) || it.first.isEqual(now) }.map {
            Pair(Duration.between(it.first, now), it)
        }.minByOrNull { it.first }?.second ?: Pair(now,
            PrayerEvent.MorningPrayer
        )
    }

    private suspend fun getPrayerList(): MutableList<Pair<LocalDateTime, PrayerEvent>> {
        val prayerTimes = mutableListOf<Pair<LocalDateTime, PrayerEvent>>()
        val eventsSchedule = prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now())
        val eventsScheduleTomorrow =
            prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now().plusDays(1))
        val eventsScheduleYesterday =
            prayerSchedulesUseCase.getPrayerSchedule(LocalDate.now().minusDays(1))
        eventsScheduleTomorrow?.let {
            prayerTimes.add(Pair(LocalTime.parse(it.morningPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now().plusDays(1)),
                PrayerEvent.MorningPrayer
            ))
        }
        eventsScheduleYesterday?.let {
            prayerTimes.add(Pair(LocalTime.parse(it.nightPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now().minusDays(1)),
                PrayerEvent.NightPrayer
            ))
        }
        eventsSchedule?.let {
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.morningPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now()),
                PrayerEvent.MorningPrayer
            ))
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.noonPrayer, timeFormatterNoSeconds)
                .atDate(LocalDate.now()),
                PrayerEvent.NoonPrayer
            ))
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.afterNoonPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now()),
                PrayerEvent.AfterNoonPrayer
            ))
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.sunsetPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now()),
                PrayerEvent.SunsetPrayer
            ))
            prayerTimes.add(Pair(LocalTime.parse(eventsSchedule.nightPrayer,
                timeFormatterNoSeconds).atDate(LocalDate.now()),
                PrayerEvent.NightPrayer
            ))
        }

        return prayerTimes
    }
}