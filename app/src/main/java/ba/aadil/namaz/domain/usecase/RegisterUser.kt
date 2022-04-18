package ba.aadil.namaz.domain.usecase

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RegisterUser {
    suspend operator fun invoke(name: String, email: String, password: String): Unit =
        suspendCoroutine { continuation ->

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        FirebaseAuth.getInstance().currentUser?.updateProfile(
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build()
                        )
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(
                            it.exception ?: FirebaseException("Unknown error")
                        )
                    }
                }
        }
}