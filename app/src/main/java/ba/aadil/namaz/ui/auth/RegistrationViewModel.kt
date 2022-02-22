package ba.aadil.namaz.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {

    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}