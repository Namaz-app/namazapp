package ba.aadil.namaz.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ba.aadil.namaz.R
import ba.aadil.namaz.prayertimes.GetNextPrayerTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit


class SchedulePrayerNotificationsJob(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {
    private val getNextPrayerTime by inject<GetNextPrayerTime>()

    override suspend fun doWork(): Result {
        val (prayerTime, prayer) = getNextPrayerTime.get()

        val builder = NotificationCompat.Builder(context, ShowNotificationsForPrayers.channelId)
            .setSmallIcon(R.drawable.ic_location_dot)
            .setContentTitle("Iduci namaz")
            .setContentText("${context.getString(prayer.nameStringId)} je za 15 minuta")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 2022)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, builder.build())
        val pendingIntent = PendingIntent.getBroadcast(context,
            2022,
            notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationTimeInSeconds =
            prayerTime.minusMinutes(15).toEpochSecond(ZoneOffset.ofTotalSeconds(0))
        val alarmManager =
            getSystemService(context, Context.ALARM_SERVICE::class.java) as AlarmManager

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
            TimeUnit.SECONDS.toMillis(notificationTimeInSeconds),
            pendingIntent)

        return Result.success()
    }

    companion object {
        const val jobName = "schedulePrayerNotificationsJob"
    }
}