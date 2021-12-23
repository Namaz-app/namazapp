package ba.aadil.namaz.di

import ba.aadil.namaz.ui.home.HomeViewModel
import ba.aadil.namaz.ui.tracking.TrackingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module {
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        TrackingViewModel(androidContext(), get())
    }
}

