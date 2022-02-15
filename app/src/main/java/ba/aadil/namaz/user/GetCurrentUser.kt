package ba.aadil.namaz.user

import com.google.firebase.auth.FirebaseAuth

class GetCurrentUser {
    fun getName(): String {
        return FirebaseAuth.getInstance().currentUser?.displayName ?: ""
    }
}