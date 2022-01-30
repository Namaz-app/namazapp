package ba.aadil.namaz

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.*
import ba.aadil.namaz.di.dataModule
import ba.aadil.namaz.di.domainModule
import ba.aadil.namaz.di.presentationModule
import ba.aadil.namaz.notifications.SchedulePrayerNotificationsJob
import ba.aadil.namaz.notifications.ShowNotificationsForPrayers
import ba.aadil.namaz.notifications.ShowPrayerNotificationsJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.concurrent.TimeUnit

class NamazApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@NamazApplication)
            modules(dataModule, domainModule, presentationModule)
        }

        createNotificationChannel()
        scheduleNotifications()
    }

    private fun scheduleNotifications() {
        val showPrayerNotificationsJob =
            OneTimeWorkRequestBuilder<ShowPrayerNotificationsJob>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        WorkManager.getInstance(this).enqueueUniqueWork(ShowPrayerNotificationsJob.jobName,
            ExistingWorkPolicy.KEEP, showPrayerNotificationsJob)

        val schedulePrayerNotifications =
            PeriodicWorkRequestBuilder<SchedulePrayerNotificationsJob>(1, TimeUnit.HOURS)
                .build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(SchedulePrayerNotificationsJob.jobName,
                ExistingPeriodicWorkPolicy.KEEP,
                schedulePrayerNotifications)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(ShowNotificationsForPrayers.channelId, name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}