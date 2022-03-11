package ba.aadil.namaz.domain.usecase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.suspendCoroutine

class LogoutUser {
    operator fun invoke(): Unit = Firebase.auth.signOut()
}
