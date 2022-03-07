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
            reminderMinutesBefore: Int,
        ) {
            val builder = NotificationCompat.Builder(context, reminderChannelId)
                .setSmallIcon(R.drawable.ic_bell)
                .addAction(0, context.getString(R.string.yes), null)
                .addAction(0, context.getString(R.string.now), null)
                .setContentTitle(context.getString(R.string.reminder_notification_text,
                    context.getString(prayer.nameStringId),
                    reminderMinutesBefore))
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            val notification = builder.build()
            with(NotificationManagerCompat.from(context)) {
                notify(reminderNotificationId, notification)
            }
        }
    }
}