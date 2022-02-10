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
        const val reminderChannelId = "namaz-prayer-notifications-reminder"
        const val notificationId = 2022
        const val reminderNotificationId = 2023

        fun showRemainingTimeNotification(
            context: Context,
            prayer: Events.Prayers,
            time: LocalDateTime,
        ): Notification {
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
                notify(notificationId, notification)
            }

            return notification
        }

        fun hide(context: Context) {
            with(NotificationManagerCompat.from(context)) {
                cancel(notificationId)
            }
        }

        fun showReminderNotification(
            context: Context,
            prayer: Events.Prayers,
            prayerTime: LocalDateTime,
            reminderMinutesBefore: Int,
        ): Boolean {
            val remainingMinutes = Duration.between(LocalDateTime.now(), prayerTime).toMinutes()
            if (remainingMinutes <= reminderMinutesBefore) {

                val builder = NotificationCompat.Builder(context, reminderChannelId)
                    .setSmallIcon(R.drawable.ic_bell)
                    .setContentTitle("Do ${context.getString(prayer.nameStringId)} je $reminderMinutesBefore minuta")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                val notification = builder.build()
                with(NotificationManagerCompat.from(context)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(reminderNotificationId, notification)
                    return true
                }
            }

            return false
        }
    }
}