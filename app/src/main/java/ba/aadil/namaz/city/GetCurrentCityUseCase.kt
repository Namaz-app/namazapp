package ba.aadil.namaz.city

interface GetCurrentCityUseCase {
    suspend fun getId(): Int
}

class GetStoredCity : GetCurrentCityUseCase {
    override suspend fun getId(): Int {
        return 77
    }
}

class MockStoredCity : GetCurrentCityUseCase {
    override suspend fun getId(): Int {
        return 77
    }
}