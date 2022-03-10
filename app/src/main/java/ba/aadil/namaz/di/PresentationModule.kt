package ba.aadil.namaz.di

import ba.aadil.namaz.ui.landing.login.LoginViewModel
import ba.aadil.namaz.ui.landing.registration.RegistrationViewModel
import ba.aadil.namaz.ui.main.dashboard.DashboardViewModel
import ba.aadil.namaz.ui.main.vaktija.HomeViewModel
import ba.aadil.namaz.ui.main.profile.ProfileViewModel
import ba.aadil.namaz.ui.main.settings.SettingsViewModel
import ba.aadil.namaz.ui.main.tracking.TrackingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module {
    viewModel {
        HomeViewModel(get(), get(), get())
    }

    viewModel {
        TrackingViewModel(androidContext(), get(), get(), get())
    }

    viewModel {
        DashboardViewModel(get(), get())
    }

    viewModel {
        ProfileViewModel(get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        RegistrationViewModel(get(), get())
    }
}

