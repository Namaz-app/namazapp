package ba.aadil.namaz.notifications

import android.content.SharedPreferences
import androidx.core.content.edit

class ToggleNotifications(private val sharedPreferences: SharedPreferences) {
    private val notificationEnabledKey = "notifications_enabled"

    fun toggle(active: Boolean) {
        sharedPreferences.edit {
            putBoolean(notificationEnabledKey, active)
        }
    }

    fun isActive(): Boolean {
        return sharedPreferences.getBoolean(notificationEnabledKey, true)
    }
}