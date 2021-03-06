package ba.aadil.namaz.notifications

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ToggleNotifications(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
) {
    private val notificationEnabledKey = "notifications_enabled"
    private val notificationReminderTimeKey = "notifications_reminder_time"

    private val _notificationOn = MutableStateFlow(isActive())
    val notificationOn = _notificationOn.asStateFlow()

    fun toggle(active: Boolean) {
        if (active) {
            val intent = Intent(context, NotificationService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
        sharedPreferences.edit {
            putBoolean(notificationEnabledKey, active)
        }
        _notificationOn.value = active
    }

    fun isActive(): Boolean {
        return sharedPreferences.getBoolean(notificationEnabledKey, true)
    }

    fun setReminderTime(minutesBefore: Int) {
        sharedPreferences.edit {
            putInt(notificationReminderTimeKey, minutesBefore)
        }
    }

    fun getReminderTime(): Int {
        return sharedPreferences.getInt(notificationReminderTimeKey, 15)
    }


    // value to index conversion for reminder time string array reminder_times
    fun getPrayerReminderTimeArrayPosition(): Int {
        return when (getReminderTime()) {
            10 -> 0
            20 -> 1
            30 -> 2
            else -> 3
        }
    }
}