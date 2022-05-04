package ba.aadil.namaz.data.db

import androidx.room.TypeConverter
import ba.aadil.namaz.domain.PrayerEvents
import java.time.Instant
import java.time.ZonedDateTime

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

object InstantConverter {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Instant): String {
            return value.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: String): Instant {
            return Instant.parse(value)
        }
}

object ZonedDateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun fromInstant(value: ZonedDateTime): String {
        return value.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toInstant(value: String): ZonedDateTime {
        return ZonedDateTime.parse(value)
    }
}

