package ba.aadil.namaz.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginUser {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    val user = it.result.user
                    if (user != null) continuation.resume(Result.success(Unit)) else continuation.resume(
                        Result.failure(it.exception ?: Exception())
                    )
                }
        }
}