package ba.aadil.namaz.ui.landing.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.domain.usecase.LoginUser
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUser: LoginUser
) : ViewModel() {

    val events = MutableSharedFlow<Event>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                loginUser(email, password)
            }.fold(
                onSuccess = {
                    events.emit(Event.NavigateToHome)
                },
                onFailure = {
                    events.emit(Event.ShowError(it.localizedMessage.toString()))
                }
            )
        }
    }

    sealed class Event {
        object NavigateToHome : Event()
        data class ShowError(val message: String) : Event()
    }
}