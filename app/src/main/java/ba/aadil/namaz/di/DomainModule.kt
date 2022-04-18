package ba.aadil.namaz.di

import ba.aadil.namaz.domain.usecase.CurrentAndAllCities
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.domain.usecase.GetCurrentDateTimeAndCity
import ba.aadil.namaz.notifications.ToggleNotifications
import ba.aadil.namaz.domain.usecase.GetNextOrCurrentPrayerTime
import ba.aadil.namaz.domain.usecase.GetPrayerTimeForDate
import ba.aadil.namaz.domain.usecase.GetPrayerTimeForDateRealz
import ba.aadil.namaz.domain.usecase.PrayerSchedulesUseCase
import ba.aadil.namaz.domain.usecase.TrackPrayerUseCase
import ba.aadil.namaz.domain.usecase.GetBadges
import ba.aadil.namaz.domain.usecase.UserRepository
import ba.aadil.namaz.domain.usecase.LoginUser
import ba.aadil.namaz.domain.usecase.LogoutUser
import ba.aadil.namaz.domain.usecase.RegisterUser
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
        GetCurrentDateTimeAndCity(get())
    }
    single {
        GetNextOrCurrentPrayerTime(get())
    }
    single {
        UserRepository(get())
    }
    single {
        GetBadges(get(), get())
    }
    single {
        LoginUser()
    }
    single {
        LogoutUser()
    }
    single {
        RegisterUser()
    }
    single {
        ToggleNotifications(androidContext(), get())
    }
}