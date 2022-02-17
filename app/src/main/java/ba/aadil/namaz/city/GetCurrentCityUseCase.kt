package ba.aadil.namaz.city

import ba.aadil.namaz.db.City
import ba.aadil.namaz.db.CityDao

interface GetCurrentCityUseCase {
    suspend fun getId(): Int
    suspend fun getName(): String
    suspend fun getAllCities(): List<City>
}

class CurrentAndAllCities(private val cityDao: CityDao) : GetCurrentCityUseCase {
    override suspend fun getId(): Int {
        return 107
    }

    override suspend fun getName(): String {
        return "Sarajevo"
    }

    override suspend fun getAllCities(): List<City> {
        return cityDao.getAllCities()
    }
}

class MockStoredCity : GetCurrentCityUseCase {
    override suspend fun getId(): Int {
        return 107
    }

    override suspend fun getName(): String {
        return "Sarajevo"
    }

    override suspend fun getAllCities(): List<City> {
        return listOf(City(0, "Sarajevo", 0, ""))
    }
}