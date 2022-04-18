package ba.aadil.namaz.data.db

import androidx.room.TypeConverter
import ba.aadil.namaz.domain.PrayerEvents
import java.time.Instant

class PrayerEventsConverter {
    @TypeConverter
    fun fromPrayerType(value: PrayerEvents): Int {
        return when (value) {
            PrayerEvents.Sunrise -> 0
            PrayerEvents.MorningPrayer -> 1
            PrayerEvents.NoonPrayer -> 2
            PrayerEvents.AfterNoonPrayer -> 3
            PrayerEvents.SunsetPrayer -> 4
            PrayerEvents.NightPrayer -> 5
        }
    }

    @TypeConverter
    fun fromIntToPrayer(value: Int): PrayerEvents {
        return when (value) {
            0 -> PrayerEvents.Sunrise
            1 -> PrayerEvents.MorningPrayer
            2 -> PrayerEvents.NoonPrayer
            3 -> PrayerEvents.AfterNoonPrayer
            4 -> PrayerEvents.SunsetPrayer
            else -> PrayerEvents.NightPrayer
        }
    }
}

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Instant): Long {
            return value.toEpochMilli()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: Long): Instant {
            return Instant.ofEpochMilli(value)
        }
    }
}

