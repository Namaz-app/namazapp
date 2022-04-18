package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.data.db.PrayerTrackingInfo
import java.time.LocalDate
import java.time.LocalTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

class GetCurrentDateTimeAndCity(private val getCurrentCityUseCase: GetCurrentCityUseCase) {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    suspend fun get(): Data {
        HijrahDate.now()
        return Data(city = getCurrentCityUseCase.getName(),
            date = "${LocalDate.now().format(PrayerTrackingInfo.dateFormatter)} / ${
                HijrahDate.now().format(PrayerTrackingInfo.hijraFormatter)
            }",
            time = LocalTime.now().format(timeFormatter))
    }


    data class Data(val city: String, val date: String, val time: String)
}