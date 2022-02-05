package ba.aadil.namaz.ui.settings

import androidx.lifecycle.ViewModel
import ba.aadil.namaz.notifications.ToggleNotifications

class SettingsViewModel(private val toggleNotifications: ToggleNotifications) : ViewModel() {
    val notificationOn = toggleNotifications.notificationOn

    fun toggleNotifications(enabled: Boolean) {
        toggleNotifications.toggle(enabled)
    }

}