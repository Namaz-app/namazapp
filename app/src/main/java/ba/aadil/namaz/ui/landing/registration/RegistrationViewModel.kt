package ba.aadil.namaz.ui.landing.registration

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.domain.User
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.domain.usecase.GetCurrentUser
import ba.aadil.namaz.domain.usecase.RegisterUser
import ba.aadil.namaz.ui.landing.login.LoginViewModel
import ba.aadil.namaz.ui.landing.onboarding.OnboardingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val getCurrentUser: GetCurrentUser,
    private val registerUser: RegisterUser
) :
    ViewModel() {
    val events = MutableSharedFlow<Event>()

    fun completeStepOne(name: String, email: String, password: String) {
        if (notBlankAndHasMinLength(name) && emailValid(email) && notBlankAndHasMinLength(password)) {
            viewModelScope.launch {
                kotlin.runCatching {
                    registerUser(name, email, password)
                }.fold(
                    onSuccess = {
                        getCurrentUser.storeName(name)
                        events.emit(Event.NavigateToOnboarding)
                    },
                    onFailure = {
                        events.emit(Event.ShowError(it.localizedMessage.toString()))
                    }
                )
            }
        }
    }

    private fun emailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun notBlankAndHasMinLength(input: String): Boolean =
        input.isNotBlank() && input.length >= 3

    sealed class Event {
        object NavigateToOnboarding : Event()
        data class ShowError(val message: String) : Event()
    }
}