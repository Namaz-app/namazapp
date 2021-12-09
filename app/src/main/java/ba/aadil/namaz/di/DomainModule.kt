package ba.aadil.namaz.di

import ba.aadil.namaz.city.GetCurrentCityUseCase
import ba.aadil.namaz.prayertimes.PrayerSchedulesUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        GetCurrentCityUseCase()
    }
    single {
        PrayerSchedulesUseCase(get(), get())
    }
}