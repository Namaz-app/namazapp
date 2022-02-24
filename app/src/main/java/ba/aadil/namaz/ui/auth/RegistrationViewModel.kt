package ba.aadil.namaz.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import ba.aadil.namaz.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistrationViewModel : ViewModel() {
    private val _transitionToStep = MutableStateFlow<RegistrationSteps>(RegistrationSteps.StepOne)
    val transitionToStep = _transitionToStep.asStateFlow()
    var user: User = User.UnAuthUser

    fun completeStepOne(email: String, password: String) {
        if (emailValid(email) && passwordValid(password)) {
            user = User.AuthedUser(email, password)
            _transitionToStep.value = RegistrationSteps.StepTwo
        }
    }

    private fun emailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun passwordValid(password: String): Boolean =
        password.isNotBlank() && password.length >= 3

    sealed class RegistrationSteps {
        object StepOne : RegistrationSteps()
        object StepTwo : RegistrationSteps()
    }
}