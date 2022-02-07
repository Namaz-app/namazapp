package ba.aadil.namaz.notifications

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import ba.aadil.namaz.prayertimes.GetNextPrayerTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class NotificationService : LifecycleService() {
    private val nextPrayerTime by inject<GetNextPrayerTime>()
    private val toggleNotifications by inject<ToggleNotifications>()
    private var isForeground = false

    override fun onCreate() {
        super.onCreate()

        val context = this
        lifecycleScope.launch {
            startForeground(context)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val context = this
        lifecycleScope.launch {
            do {
                startForeground(context)
                delay(TimeUnit.MINUTES.toMillis(1))
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

        return START_NOT_STICKY
    }

    private suspend fun startForeground(context: NotificationService) {
        if (toggleNotifications.isActive()) {
            val (time, prayer) = withContext(Dispatchers.IO) { nextPrayerTime.get() }
            val notification = ShowNotificationsForPrayers.show(context, prayer, time)

            if (!isForeground) {
                startForeground(ShowNotificationsForPrayers.notificationId, notification)
                isForeground = true
            }
        }
    }
}