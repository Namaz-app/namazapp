package ba.aadil.namaz.di

import ba.aadil.namaz.domain.usecase.CurrentAndAllCities
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.domain.usecase.GetCurrentDateTimeAndCity
import ba.aadil.namaz.domain.usecase.GetEmojiAndCongratsForPrayedPrayers
import ba.aadil.namaz.notifications.ToggleNotifications
import ba.aadil.namaz.domain.usecase.GetNextOrCurrentPrayerTime
import ba.aadil.namaz.domain.usecase.GetPrayerTimeForDate
import ba.aadil.namaz.domain.usecase.GetPrayerTimeForDateRealz
import ba.aadil.namaz.domain.usecase.PrayerSchedulesUseCase
import ba.aadil.namaz.domain.usecase.GetStatisticsUseCase
import ba.aadil.namaz.domain.usecase.TrackPrayerUseCase
import ba.aadil.namaz.domain.usecase.GetBadges
import ba.aadil.namaz.domain.usecase.GetCurrentUser
import ba.aadil.namaz.domain.usecase.LoginUser
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    single<GetCurrentCityUseCase> {
        CurrentAndAllCities(get(), get())
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
        GetNextOrCurrentPrayerTime(get())
    }
    single {
        GetCurrentUser(get())
    }
    single {
        GetBadges(get(), get())
    }
    single {
        LoginUser()
    }
    single {
        ToggleNotifications(androidContext(), get())
    }
}