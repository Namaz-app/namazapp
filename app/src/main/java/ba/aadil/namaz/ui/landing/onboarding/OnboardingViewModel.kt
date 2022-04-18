package ba.aadil.namaz.ui.landing.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.data.db.City
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.domain.usecase.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getCurrentCityUseCase: GetCurrentCityUseCase,
    private val userRepository: UserRepository,
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
            userRepository.storeBirthday(birthdayYear)
            events.emit(Event.NavigateToHome)
        }
    }

    sealed class Event {
        object NavigateToHome : Event()
    }
}