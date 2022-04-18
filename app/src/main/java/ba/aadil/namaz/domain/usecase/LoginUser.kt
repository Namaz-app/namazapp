package ba.aadil.namaz.domain.usecase

import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginUser {
    suspend operator fun invoke(email: String, password: String): Unit =
        suspendCoroutine { continuation ->
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // Sign in success, update UI with the signed-in user's information
                        continuation.resume(Unit)
                    } else {
                        // If sign in fails, display a message to the user.
                        continuation.resumeWithException(
                            task.exception ?: FirebaseException("Unknown error")
                        )
                    }
                }
        }
}