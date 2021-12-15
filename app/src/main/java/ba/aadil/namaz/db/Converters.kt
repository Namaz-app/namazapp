package ba.aadil.namaz.db

import androidx.room.TypeConverter
import ba.aadil.namaz.prayertimes.Events

class Converters {
    @TypeConverter
    fun fromPrayerType(value: Events.Prayers): Int {
        return when (value) {
            Events.Prayers.MorningPrayer -> 0
            Events.Prayers.NoonPrayer -> 1
            Events.Prayers.AfterNoonPrayer -> 2
            Events.Prayers.SunsetPrayer -> 3
            Events.Prayers.NightPrayer -> 4
        }
    }

    @TypeConverter
    fun fromIntToPrayer(value: Int): Events.Prayers {
        return when (value) {
            0 -> Events.Prayers.MorningPrayer
            1 -> Events.Prayers.NoonPrayer
            2 -> Events.Prayers.AfterNoonPrayer
            3 -> Events.Prayers.SunsetPrayer
            else -> Events.Prayers.NightPrayer
        }
    }
}