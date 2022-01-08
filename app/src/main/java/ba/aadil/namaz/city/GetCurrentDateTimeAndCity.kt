package ba.aadil.namaz.city

import ba.aadil.namaz.db.Track
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime

class GetCurrentDateTimeAndCity(private val getCurrentCityUseCase: GetCurrentCityUseCase) {
    suspend fun get(): Data {
        return Data(city = getCurrentCityUseCase.getName(),
            date = LocalDate.now().format(Track.dateFormatter),
            time = LocalDateTime.now().format(Track.dateTimeFormatter))
    }


    data class Data(val city: String, val date: String, val time: String) : ViewModel
}