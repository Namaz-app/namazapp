package ba.aadil.namaz.city

interface GetCurrentCityUseCase {
    suspend fun getId(): Int
    suspend fun getName(): String
}

class GetStoredCity : GetCurrentCityUseCase {
    override suspend fun getId(): Int {
        return 107
    }

    override suspend fun getName(): String {
        return "Sarajevo"
    }
}

class MockStoredCity : GetCurrentCityUseCase {
    override suspend fun getId(): Int {
        return 107
    }

    override suspend fun getName(): String {
        return "Sarajevo"
    }
}