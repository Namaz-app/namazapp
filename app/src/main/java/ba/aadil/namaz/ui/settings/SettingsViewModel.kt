package ba.aadil.namaz.ui.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel

class SettingsViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    fun toggleNotifications(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(notificationEnabledKey, enabled)
        }
    }

    companion object {
        const val notificationEnabledKey = "notifications_enabled"
    }
}