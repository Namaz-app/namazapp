package ba.aadil.namaz.di

import ba.aadil.namaz.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module {
    viewModel {
        HomeViewModel(get())
    }
}

