package ba.aadil.namaz.ui.main.settings

import androidx.lifecycle.ViewModel
import ba.aadil.namaz.domain.usecase.LoginUser
import ba.aadil.namaz.domain.usecase.LogoutUser
import ba.aadil.namaz.notifications.ToggleNotifications

class SettingsViewModel(
    private val toggleNotifications: ToggleNotifications,
    private val logoutUser: LogoutUser
    ) : ViewModel() {
    val notificationOn = toggleNotifications.notificationOn

    fun toggleNotifications(enabled: Boolean) {
        toggleNotifications.toggle(enabled)
    }

    // position to int conversion based on string arrays reminder_times in strings.xml
    fun setPrayerReminderTime(position: Int) {
        val minutesBefore = when (position) {
            0 -> 10
            1 -> 20
            2 -> 30
            else -> 60
        }

        toggleNotifications.setReminderTime(minutesBefore)
    }

    fun getPrayerReminderTime(): Int {
        return toggleNotifications.getReminderTime()
    }

    fun logout() {
        logoutUser()
    }

    fun getPrayerReminderTimeArrayPosition(): Int {
        return toggleNotifications.getPrayerReminderTimeArrayPosition()
    }
}