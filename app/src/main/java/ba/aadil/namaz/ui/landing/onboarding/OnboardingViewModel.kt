package ba.aadil.namaz.ui.landing.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.data.db.City
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.domain.usecase.GetCurrentUser
import ba.aadil.namaz.ui.landing.login.LoginViewModel
import ba.aadil.namaz.ui.landing.registration.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getCurrentCityUseCase: GetCurrentCityUseCase,
    private val getCurrentUser: GetCurrentUser,
) : ViewModel() {

    val events = MutableSharedFlow<Event>()
    val cities = MutableStateFlow<List<City>>(emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            cities.value = getCurrentCityUseCase.getAllCities()
        }
    }

    fun updateUserInfo(currentCityId: Int, birthdayYear: Int) {
        viewModelScope.launch {
            getCurrentCityUseCase.setCurrentCity(currentCityId)
            getCurrentUser.storeBirthday(birthdayYear)
            events.emit(Event.NavigateToHome)
        }
    }

    sealed class Event {
        object NavigateToHome : Event()
    }
}