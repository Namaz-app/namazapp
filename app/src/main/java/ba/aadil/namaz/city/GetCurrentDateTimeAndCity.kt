package ba.aadil.namaz.city

import ba.aadil.namaz.db.Track
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class GetCurrentDateTimeAndCity(private val getCurrentCityUseCase: GetCurrentCityUseCase) {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    suspend fun get(): Data {
        return Data(city = getCurrentCityUseCase.getName(),
            date = LocalDate.now().format(Track.dateFormatter),
            time = LocalTime.now().format(timeFormatter))
    }


    data class Data(val city: String, val date: String, val time: String)
}