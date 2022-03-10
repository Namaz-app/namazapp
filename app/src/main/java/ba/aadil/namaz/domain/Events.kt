package ba.aadil.namaz.domain

import ba.aadil.namaz.R

sealed class Events {
    object Sunrise : Events()
    sealed class Prayers(val sortWeight: Int, val nameStringId: Int) : Events() {
        object MorningPrayer : Prayers(1, R.string.morningPrayer)
        object NoonPrayer : Prayers(2, R.string.noonPrayer)
        object AfterNoonPrayer : Prayers(3, R.string.afternoonPrayer)
        object SunsetPrayer : Prayers(4, R.string.sunsetPrayer)
        object NightPrayer : Prayers(5, R.string.nightPrayer)
        companion object {
            fun fromSortWeight(weight: Int): Prayers {
                return when (weight) {
                    MorningPrayer.sortWeight -> MorningPrayer
                    NoonPrayer.sortWeight -> MorningPrayer
                    AfterNoonPrayer.sortWeight -> MorningPrayer
                    SunsetPrayer.sortWeight -> MorningPrayer
                    else -> NightPrayer
                }
            }
        }
    }
}