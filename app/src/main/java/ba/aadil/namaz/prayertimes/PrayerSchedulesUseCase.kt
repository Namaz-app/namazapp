package ba.aadil.namaz.prayertimes

import ba.aadil.namaz.db.OffsetDao
import ba.aadil.namaz.db.PrayerScheduleDao
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PrayerSchedulesUseCase(
    private val prayerScheduleDao: PrayerScheduleDao,
    private val offsetDao: OffsetDao
) {
    private val prayerTimeFormat = DateTimeFormatter.ofPattern("HH:mm")

    fun getPrayerSchedule(date: LocalDate, locationId: Int): PrayersSchedule? {
        val monthWithLeadingZero = String.format(
            "%02d",
            date.month.value
        )
        val dayWithLeadingZero = String.format("%02d", date.dayOfMonth)
        val prayers =
            prayerScheduleDao.getPrayersForADay("$monthWithLeadingZero-$dayWithLeadingZero")
        val offset = offsetDao.getOffsetForACity(date.monthValue, locationId)

        prayers.firstOrNull()?.let { dailyPrayer ->
            offset.firstOrNull()?.let { offset ->
                LocalTime.parse(dailyPrayer.noonPrayer)
                val noonPrayerTime = LocalTime.parse(dailyPrayer.noonPrayer, prayerTimeFormat)
                    .plus(offset.noonPrayerOffset.toLong(), ChronoUnit.MINUTES)
                val morningPrayer = LocalTime.parse(dailyPrayer.morningPrayer, prayerTimeFormat)
                    .plus(offset.morningPrayerOffset.toLong(), ChronoUnit.MINUTES)

                return PrayersSchedule(
                    morningPrayer = prayerTimeFormat.format(morningPrayer),
                    noonPrayer = prayerTimeFormat.format(noonPrayerTime)
                )
            }
        }

        return null
    }
}

data class PrayersSchedule(val morningPrayer: String, val noonPrayer: String)