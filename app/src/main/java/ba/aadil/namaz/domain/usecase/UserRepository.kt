package ba.aadil.namaz.domain.usecase

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth

class UserRepository(val sharedPreferences: SharedPreferences) {
    private val userNameKey = "user_name"
    private val userBirthdayKey = "user_birthday"

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

    fun storeBirthday(year: Int) {
        sharedPreferences.edit {
            putInt(userBirthdayKey, year)
        }
    }
    fun completedOnboarding(): Boolean {
        return sharedPreferences.getBoolean("onboarding_completed", true)
    }
}