package ba.aadil.namaz.data.db

import androidx.room.TypeConverter
import ba.aadil.namaz.domain.PrayerEvent
import java.time.Instant
import java.time.LocalDate

class PrayerEventsConverter {
    @TypeConverter
    fun fromPrayerType(value: PrayerEvent): Int {
        return when (value) {
            PrayerEvent.Sunrise -> 0
            PrayerEvent.MorningPrayer -> 1
            PrayerEvent.NoonPrayer -> 2
            PrayerEvent.AfterNoonPrayer -> 3
            PrayerEvent.SunsetPrayer -> 4
            PrayerEvent.NightPrayer -> 5
        }
    }

    @TypeConverter
    fun fromIntToPrayer(value: Int): PrayerEvent {
        return when (value) {
            0 -> PrayerEvent.Sunrise
            1 -> PrayerEvent.MorningPrayer
            2 -> PrayerEvent.NoonPrayer
            3 -> PrayerEvent.AfterNoonPrayer
            4 -> PrayerEvent.SunsetPrayer
            else -> PrayerEvent.NightPrayer
        }
    }
}

class InstantConverter {
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

class LocalDateConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: LocalDate): String {
            return value.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: String): LocalDate {
            return LocalDate.parse(value)
        }
    }
}


