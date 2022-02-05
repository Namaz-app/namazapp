package ba.aadil.namaz.notifications

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ToggleNotifications(private val sharedPreferences: SharedPreferences) {
    private val notificationEnabledKey = "notifications_enabled"
    private val _notificationOn = MutableStateFlow(isActive())
    val notificationOn = _notificationOn.asStateFlow()

    fun toggle(active: Boolean) {
        sharedPreferences.edit {
            putBoolean(notificationEnabledKey, active)
        }
        _notificationOn.value = active
    }

    fun isActive(): Boolean {
        return sharedPreferences.getBoolean(notificationEnabledKey, true)
    }
}