package ba.aadil.namaz.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ba.aadil.namaz.ui.main.MainActivity
import ba.aadil.namaz.R
import ba.aadil.namaz.domain.PrayerEvent
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class ShowNotificationsForPrayers {
    companion object {
        const val channelId = "namaz-prayer-notifications"
        const val reminderChannelId = "namaz-prayer-notifications-reminder"
        const val notificationId = 2022
        const val reminderNotificationId = 2023
        const val markCurrentPrayerAsPrayedKey = "mark-current-prayer-as-prayed"
        private val shownReminders =
            Collections.synchronizedMap(object :
                LinkedHashMap<Pair<PrayerEvent, LocalDate>, Boolean>() {
                override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Pair<PrayerEvent, LocalDate>, Boolean>?): Boolean {
                    return size >= 10
                }
            })

        fun showRemainingTimeNotification(
            context: Context,
            prayer: PrayerEvent,
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
            nextPrayer: PrayerEvent,
            currentPrayer: PrayerEvent,
            reminderMinutesBefore: Int,
        ) {
            val mainActivityPendingIntent = Intent(context, MainActivity::class.java).apply {
                putExtra("prayer", currentPrayer.sortWeight)
            }
            mainActivityPendingIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            mainActivityPendingIntent.putExtra(markCurrentPrayerAsPrayedKey, true)

            val pendingIntent = PendingIntent.getActivity(context,
                2024,
                mainActivityPendingIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(context, reminderChannelId)
                .setSmallIcon(R.drawable.ic_bell)
                .addAction(0, context.getString(R.string.yes), pendingIntent)
                .addAction(0, context.getString(R.string.now), pendingIntent)
                .setContentTitle(context.getString(R.string.reminder_notification_text,
                    context.getString(nextPrayer.nameStringId),
                    reminderMinutesBefore))
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            val notification = builder.build()
            with(NotificationManagerCompat.from(context)) {
                notify(reminderNotificationId, notification)
            }
            shownReminders[Pair(nextPrayer, LocalDate.now())] = true
        }

        fun markAsShown(prayer: PrayerEvent, localDate: LocalDate) {
            val key = Pair(prayer, localDate)
            shownReminders[key] = true
        }

        fun shouldShowRemainderNotification(prayer: PrayerEvent): Boolean {
            val key = Pair(prayer, LocalDate.now())
            return shownReminders[key] == false || !shownReminders.containsKey(key)
        }

        fun shownRemindersSize(): Int {
            return shownReminders.size
        }
    }
}