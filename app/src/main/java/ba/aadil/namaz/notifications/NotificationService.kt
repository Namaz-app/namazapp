package ba.aadil.namaz.notifications

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import ba.aadil.namaz.domain.usecase.GetNextOrCurrentPrayerTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/*
Notification service, responsible for showing an up to date notification for remaining time
till next prayer (silent because its updating every minute), also a reminder notification
which is not silent to notify the user of upcoming prayer
 */
class NotificationService : LifecycleService() {
    private val nextPrayerTime by inject<GetNextOrCurrentPrayerTime>()
    private val toggleNotifications by inject<ToggleNotifications>()
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
            val (time, nextPrayer) = withContext(Dispatchers.IO) { nextPrayerTime.getNext() }
            val notification =
                ShowNotificationsForPrayers.showRemainingTimeNotification(context, nextPrayer, time)

            if (ShowNotificationsForPrayers.shouldShowRemainderNotification(nextPrayer)) {
                val remainingMinutes = Duration.between(LocalDateTime.now(), time).toMinutes()
                val reminderMinutesBefore = toggleNotifications.getReminderTime()
                if (remainingMinutes <= reminderMinutesBefore) {
                    val (_, currentPrayer) = withContext(Dispatchers.IO) { nextPrayerTime.getCurrent() }
                    ShowNotificationsForPrayers.showReminderNotification(context,
                        nextPrayer,
                        currentPrayer,
                        reminderMinutesBefore)
                }
                ShowNotificationsForPrayers.markAsShown(nextPrayer, LocalDate.now())
            }

            if (!isForeground) {
                startForeground(ShowNotificationsForPrayers.notificationId, notification)
                isForeground = true
            }
        }
    }
}