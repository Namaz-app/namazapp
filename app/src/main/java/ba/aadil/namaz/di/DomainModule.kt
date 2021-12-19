package ba.aadil.namaz.di

import ba.aadil.namaz.city.GetCurrentCityUseCase
import ba.aadil.namaz.city.GetStoredCity
import ba.aadil.namaz.prayertimes.GetPrayerTimeForDate
import ba.aadil.namaz.prayertimes.GetPrayerTimeForDateRealz
import ba.aadil.namaz.prayertimes.PrayerSchedulesUseCase
import ba.aadil.namaz.tracking.TrackPrayerUseCase
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
}