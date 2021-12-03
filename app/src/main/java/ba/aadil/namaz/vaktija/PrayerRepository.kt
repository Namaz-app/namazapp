package ba.aadil.namaz.vaktija

import ba.aadil.namaz.ui.db.OffsetDao
import ba.aadil.namaz.ui.db.PrayerScheduleDao
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PrayerRepository(
    private val prayerScheduleDao: PrayerScheduleDao,
    private val offsetDao: OffsetDao
) {
    private val prayerTimeFormat = DateTimeFormatter.ofPattern("HH:mm")

    suspend fun timeForNoonPrayer(date: LocalDate, locationId: Int): String? {
        val monthWithLeadingZero = String.format(
            "%02d",
            date.month.value + 1
        )
        val dayWithLeadingZero = String.format("%02d", date.dayOfMonth)
        val prayers =
            prayerScheduleDao.getPrayersForADay("$monthWithLeadingZero-$dayWithLeadingZero")
        val offset = offsetDao.getOffsetForACity(date.monthValue + 1, locationId)

        prayers.firstOrNull()?.let { dailyPrayer ->
            offset.firstOrNull()?.let { offset ->
                LocalTime.parse(dailyPrayer.noonPrayer)
                val newDate = LocalTime.parse(dailyPrayer.noonPrayer, prayerTimeFormat)
                    .plus(offset.noonPrayerOffset.toLong(), ChronoUnit.MINUTES)

                return prayerTimeFormat.format(newDate)
            }
        }

        return null
    }
}