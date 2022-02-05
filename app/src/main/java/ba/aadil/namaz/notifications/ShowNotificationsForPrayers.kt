package ba.aadil.namaz.notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ba.aadil.namaz.R
import ba.aadil.namaz.prayertimes.Events
import java.time.Duration
import java.time.LocalDateTime

class ShowNotificationsForPrayers {
    companion object {
        const val channelId = "namaz-prayer-notifications"
        fun show(context: Context, prayer: Events.Prayers, time: LocalDateTime): Notification {
            val duration = Duration.between(LocalDateTime.now(), time)
            val hours = duration.toHours()
            val minutes = duration.minusHours(hours).toMinutes()
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_location_dot)
                .setContentTitle("${context.getString(prayer.nameStringId)} je za ${hours}h ${minutes}minuta")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notification = builder.build()
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(2022, notification)
            }

            return notification
        }
    }
}