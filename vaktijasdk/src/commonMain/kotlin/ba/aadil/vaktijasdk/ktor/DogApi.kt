package ba.aadil.vaktijasdk.ktor

import ba.aadil.vaktijasdk.response.BreedResult

interface DogApi {
    suspend fun getJsonFromApi(): BreedResult
}
