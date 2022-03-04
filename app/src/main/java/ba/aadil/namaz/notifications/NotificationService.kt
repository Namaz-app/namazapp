package ba.aadil.namaz.notifications

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.prayertimes.GetNextPrayerTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

/*
Notification service, responsible for showing an up to date notification for remaining time
till next prayer (silent because its updating every minute), also a reminder notification
which is not silent to notify the user of upcoming prayer
 */
class NotificationService : LifecycleService() {
    private val nextPrayerTime by inject<GetNextPrayerTime>()
    private val toggleNotifications by inject<ToggleNotifications>()
    private val shownReminders = hashMapOf<Events.Prayers, Boolean>()
    private var isForeground = false

    override fun onCreate() {
        super.onCreate()

        val context = this
        lifecycleScope.launch {
            startForegroundAndShowNotifications(context)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        /*
        Two jobs, one for canceling foreground service, it runs every 2 seconds.
        The other jobs is for showing remaining time till next prayer
         */
        val context = this
        lifecycleScope.launch {
            do {
                startForegroundAndShowNotifications(context)
                delay(TimeUnit.SECONDS.toMillis(10))
            } while (isForeground)
        }

        lifecycleScope.launch {
            do {
                if (!toggleNotifications.isActive()) {
                    stopForeground(true)
                    isForeground = false
                }
                delay(TimeUnit.SECONDS.toMillis(2))
            } while (isForeground)
        }

        return START_STICKY
    }

    /*
    this shows two sets of notifications, one for remaining time till next prayer.
    Other one for reminders just before the prayer, but we don't want to show multiple
    reminder notifications.
     */
    private suspend fun startForegroundAndShowNotifications(context: NotificationService) {
        if (toggleNotifications.isActive()) {
            val (time, prayer) = withContext(Dispatchers.IO) { nextPrayerTime.get() }
            val notification =
                ShowNotificationsForPrayers.showRemainingTimeNotification(context, prayer, time)

            if (shownReminders[prayer] == false || !shownReminders.containsKey(prayer)) {
                val didShow = ShowNotificationsForPrayers.showReminderNotification(context,
                    prayer,
                    time,
                    toggleNotifications.getReminderTime())
                shownReminders[prayer] = didShow
            }

            if (!isForeground) {
                startForeground(ShowNotificationsForPrayers.notificationId, notification)
                isForeground = true
            }
        }
    }
}