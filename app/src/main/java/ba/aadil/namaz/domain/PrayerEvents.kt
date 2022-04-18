package ba.aadil.namaz.domain

import ba.aadil.namaz.R

sealed class PrayerEvents(val sortWeight: Int, val nameStringId: Int) {
    object Sunrise : PrayerEvents(sortWeight = 0,R.string.sunrise)
    object MorningPrayer : PrayerEvents(1, R.string.morningPrayer)
    object NoonPrayer : PrayerEvents(2, R.string.noonPrayer)
    object AfterNoonPrayer : PrayerEvents(3, R.string.afternoonPrayer)
    object SunsetPrayer : PrayerEvents(4, R.string.sunsetPrayer)
    object NightPrayer : PrayerEvents(5, R.string.nightPrayer)
    companion object {
        fun fromSortWeight(weight: Int): PrayerEvents {
            return when (weight) {
                MorningPrayer.sortWeight -> MorningPrayer
                NoonPrayer.sortWeight -> NoonPrayer
                AfterNoonPrayer.sortWeight -> AfterNoonPrayer
                SunsetPrayer.sortWeight -> SunsetPrayer
                else -> NightPrayer
            }
        }
    }
}