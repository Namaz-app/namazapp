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

class NotificationService : LifecycleService() {
    val nextPrayerTime by inject<GetNextPrayerTime>()

    private var isForeground = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val context = this
        lifecycleScope.launch {
            val (time, prayer) = withContext(Dispatchers.IO) { nextPrayerTime.get() }
            val notification = ShowNotificationsForPrayers.show(context, prayer, time)

            if (!isForeground) {
                startForeground(2022, notification)
                isForeground = true
            }

            delay(1000)
        }

        return START_NOT_STICKY
    }
}