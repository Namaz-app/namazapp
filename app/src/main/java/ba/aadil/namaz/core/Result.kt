package ba.aadil.namaz.core

data class Result<T>(
    val data: T?,
    val error: Pair<Exception?, String>?
)
