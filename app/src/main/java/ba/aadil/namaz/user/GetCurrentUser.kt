package ba.aadil.namaz.user

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

class GetCurrentUser(val sharedPreferences: SharedPreferences) {
    fun getName(): String {
        return FirebaseAuth
            .getInstance()
            .currentUser
            ?.displayName
            ?.split(" ")
            ?.firstOrNull()
            ?: ""
    }

    fun completedOnboarding(): Boolean {
        return sharedPreferences.getBoolean("onboarding_completed", true)
    }

}