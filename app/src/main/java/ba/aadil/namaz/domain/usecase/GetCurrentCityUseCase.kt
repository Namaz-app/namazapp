package ba.aadil.namaz.domain.usecase

import android.content.SharedPreferences
import androidx.core.content.edit
import ba.aadil.namaz.data.db.City
import ba.aadil.namaz.data.db.CityDao

interface GetCurrentCityUseCase {
    suspend fun getId(): Int
    suspend fun getName(): String
    fun setCurrentCity(dbId: Int)
    suspend fun getAllCities(): List<City>
}

class CurrentAndAllCities(
    private val cityDao: CityDao,
    private val sharedPreferences: SharedPreferences,
) : GetCurrentCityUseCase {
    private val currentCityIdKey = "current_city_id"

    override suspend fun getId(): Int {
        val currentCityId = sharedPreferences.getInt(currentCityIdKey, City.defaultCityId)
        return cityDao.getCity(currentCityId).firstOrNull()?._id ?: City.defaultCityId
    }

    override fun setCurrentCity(dbId: Int) {
        sharedPreferences.edit {
            putInt(currentCityIdKey, dbId)
        }
    }

    override suspend fun getName(): String {
        val currentCityId = sharedPreferences.getInt(currentCityIdKey, City.defaultCityId)
        return cityDao.getCity(currentCityId).firstOrNull()?.name ?: "Sarajevo"
    }

    override suspend fun getAllCities(): List<City> {
        return cityDao.getAllCities()
    }
}

class MockStoredCity : GetCurrentCityUseCase {
    override suspend fun getId(): Int {
        return City.defaultCityId
    }

    override suspend fun getName(): String {
        return "Sarajevo"
    }

    override fun setCurrentCity(dbId: Int) {
    }

    override suspend fun getAllCities(): List<City> {
        return listOf(City(0, "Sarajevo", 0, ""))
    }
}