package ba.aadil.namaz.prayertimes

sealed class Events {
    object Sunrise : Events()
    sealed class Prayers(val sortWeight: Int) : Events() {
        object MorningPrayer : Prayers(1)
        object NoonPrayer : Prayers(2)
        object AfterNoonPrayer : Prayers(3)
        object SunsetPrayer : Prayers(4)
        object NightPrayer : Prayers(5)
    }
}