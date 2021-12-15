package ba.aadil.namaz.prayertimes

sealed class Events {
    object Sunrise : Events()
    sealed class Prayers : Events() {
        object MorningPrayer : Prayers()
        object NoonPrayer : Prayers()
        object AfterNoonPrayer : Prayers()
        object SunsetPrayer : Prayers()
        object NightPrayer : Prayers()
    }
}