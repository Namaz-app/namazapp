package ba.aadil.namaz.user

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth

class GetCurrentUser(val sharedPreferences: SharedPreferences) {
    private val userNameKey = "user_name"
    fun getName(): String {
        return sharedPreferences.getString(userNameKey, FirebaseAuth
            .getInstance()
            .currentUser
            ?.displayName
            ?.split(" ")
            ?.firstOrNull()) ?: ""
    }

    fun storeName(name: String) {
        sharedPreferences.edit {
            putString(userNameKey, name)
        }
    }

    fun completedOnboarding(): Boolean {
        return sharedPreferences.getBoolean("onboarding_completed", true)
    }

}