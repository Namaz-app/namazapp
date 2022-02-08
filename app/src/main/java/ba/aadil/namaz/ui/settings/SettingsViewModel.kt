package ba.aadil.namaz.ui.settings

import androidx.lifecycle.ViewModel
import ba.aadil.namaz.notifications.ToggleNotifications

class SettingsViewModel(private val toggleNotifications: ToggleNotifications) : ViewModel() {
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

    fun getPrayerReminderTimeArrayPosition(): Int {
        return toggleNotifications.getPrayerReminderTimeArrayPosition()
    }
}