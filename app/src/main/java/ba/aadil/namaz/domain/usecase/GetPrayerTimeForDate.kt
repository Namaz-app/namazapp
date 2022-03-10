package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.domain.Events
import java.time.LocalDate
import java.time.LocalDateTime

interface GetPrayerTimeForDate {
    suspend fun get(date: LocalDate, event: Events.Prayers): Result<LocalDateTime>
}

class GetPrayerTimeForDateRealz(private val prayerSchedulesUseCase: PrayerSchedulesUseCase) :
    GetPrayerTimeForDate {
    override suspend fun get(date: LocalDate, event: Events.Prayers): Result<LocalDateTime> {
        val result = prayerSchedulesUseCase.getPrayerScheduleMap(date)
        return result.mapCatching {
            val time = it.map.getOrDefault(event, "00:00")
            val hourMinute = time.split(":")
            val hour = Integer.parseInt(hourMinute.first())
            val minute = Integer.parseInt(hourMinute.last())
            val prayerDateTime = date.atTime(hour, minute)
            return Result.success(prayerDateTime)
        }
    }
}