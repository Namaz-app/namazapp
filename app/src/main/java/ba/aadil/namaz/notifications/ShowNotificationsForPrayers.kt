package ba.aadil.namaz.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ba.aadil.namaz.R
import ba.aadil.namaz.prayertimes.Events

class ShowNotificationsForPrayers {
    companion object {
        const val channelId = "namaz-prayer-notifications"
        fun show(context: Context, prayer: Events.Prayers) {
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_location_dot)
                .setContentTitle("Iduci namaz")
                .setContentText("${context.getString(prayer.nameStringId)} je za 15 minuta")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(2022, builder.build())
            }
        }
    }
}