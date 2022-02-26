package ba.aadil.namaz.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import ba.aadil.namaz.auth.User
import ba.aadil.namaz.city.GetCurrentCityUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistrationViewModel(private val getCurrentCityUseCase: GetCurrentCityUseCase) :
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

    fun completeStepOne(email: String, password: String) {
        if (emailValid(email) && passwordValid(password)) {
            user = User.AuthedUser(email, password)
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _transitionToStep.value = RegistrationSteps.StepTwo
                    } else {
                        _errors.tryEmit("Error creating user ${it.exception}")
                    }
                }
        }
    }

    fun completeStepTwo(currentCityId: Int) {
        getCurrentCityUseCase.setCurrentCity(currentCityId)
        _transitionToStep.value = RegistrationSteps.RedirectToMain
    }

    private fun emailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun passwordValid(password: String): Boolean =
        password.isNotBlank() && password.length >= 3

    sealed class RegistrationSteps {
        object StepOne : RegistrationSteps()
        object StepTwo : RegistrationSteps()
        object RedirectToMain : RegistrationSteps()
    }
}