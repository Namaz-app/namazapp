package ba.aadil.namaz.di

import ba.aadil.namaz.city.GetCurrentCityUseCase
import ba.aadil.namaz.city.GetCurrentDateTimeAndCity
import ba.aadil.namaz.city.GetStoredCity
import ba.aadil.namaz.motivation.GetEmojiAndCongratsForPrayedPrayers
import ba.aadil.namaz.notifications.ToggleNotifications
import ba.aadil.namaz.prayertimes.GetNextPrayerTime
import ba.aadil.namaz.prayertimes.GetPrayerTimeForDate
import ba.aadil.namaz.prayertimes.GetPrayerTimeForDateRealz
import ba.aadil.namaz.prayertimes.PrayerSchedulesUseCase
import ba.aadil.namaz.stats.GetStatisticsUseCase
import ba.aadil.namaz.tracking.TrackPrayerUseCase
import ba.aadil.namaz.user.GetBadges
import ba.aadil.namaz.user.GetCurrentUser
import org.koin.dsl.module

val domainModule = module {
    single<GetCurrentCityUseCase> {
        GetStoredCity()
    }
    single {
        PrayerSchedulesUseCase(get(), get(), get())
    }
    single<GetPrayerTimeForDate> {
        GetPrayerTimeForDateRealz(get())
    }
    single {
        TrackPrayerUseCase(get(), get())
    }
    single {
        GetStatisticsUseCase(get())
    }
    single {
        GetEmojiAndCongratsForPrayedPrayers()
    }
    single {
        GetCurrentDateTimeAndCity(get())
    }
    single {
        GetNextPrayerTime(get())
    }
    single {
        GetCurrentUser()
    }
    single {
        GetBadges(get(), get())
    }
    single {
        ToggleNotifications(get())
    }
}