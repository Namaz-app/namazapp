package ba.aadil.namaz.ui.landing.registration

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.domain.User
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.domain.usecase.GetCurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val getCurrentUser: GetCurrentUser,
    private val getCurrentCityUseCase: GetCurrentCityUseCase,
) :
    ViewModel() {
    private val _transitionToStep = MutableStateFlow<RegistrationSteps>(RegistrationSteps.StepOne)
    private val _errors = MutableSharedFlow<String>()
    val errors = _errors.asSharedFlow()
    val transitionToStep = _transitionToStep.asStateFlow()
    var user: User = User.UnAuthUser

    init {
        if (FirebaseAuth.getInstance().currentUser == null) {
            _transitionToStep.value = RegistrationSteps.StepOne
        } else {
            _transitionToStep.value = RegistrationSteps.StepTwo
        }
    }

    fun completeStepOne(name: String, email: String, password: String) {
        if (notBlankAndHasMinLength(name) && emailValid(email) && notBlankAndHasMinLength(password)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        getCurrentUser.storeName(name)
                        FirebaseAuth.getInstance().currentUser?.updateProfile(
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build())
                        _transitionToStep.value = RegistrationSteps.StepTwo
                    } else {
                        viewModelScope.launch {
                            _errors.emit("Error creating user ${it.exception}")
                        }
                    }
                }
        }
    }

    fun completeStepTwo(currentCityId: Int, birthdayYear: Int) {
        getCurrentCityUseCase.setCurrentCity(currentCityId)
        getCurrentUser.storeBirthday(birthdayYear)
        _transitionToStep.value = RegistrationSteps.RedirectToMain
    }

    private fun emailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun notBlankAndHasMinLength(input: String): Boolean =
        input.isNotBlank() && input.length >= 3

    sealed class RegistrationSteps {
        object StepOne : RegistrationSteps()
        object StepTwo : RegistrationSteps()
        object RedirectToMain : RegistrationSteps()
    }
}