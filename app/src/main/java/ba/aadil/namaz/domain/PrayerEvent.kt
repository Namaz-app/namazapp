package ba.aadil.namaz.domain

import ba.aadil.namaz.R

sealed class PrayerEvent(val sortWeight: Int, val nameStringId: Int) {
    object Sunrise : PrayerEvent(sortWeight = 0,R.string.sunrise)
    object MorningPrayer : PrayerEvent(1, R.string.morningPrayer)
    object NoonPrayer : PrayerEvent(2, R.string.noonPrayer)
    object AfterNoonPrayer : PrayerEvent(3, R.string.afternoonPrayer)
    object SunsetPrayer : PrayerEvent(4, R.string.sunsetPrayer)
    object NightPrayer : PrayerEvent(5, R.string.nightPrayer)
    companion object {
        fun fromSortWeight(weight: Int): PrayerEvent {
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