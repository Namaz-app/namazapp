package ba.aadil.namaz

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.*
import ba.aadil.namaz.di.dataModule
import ba.aadil.namaz.di.domainModule
import ba.aadil.namaz.di.presentationModule
import ba.aadil.namaz.notifications.NotificationService
import ba.aadil.namaz.notifications.ShowNotificationsForPrayers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class NamazApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@NamazApplication)
            modules(dataModule, domainModule, presentationModule)
        }

        createNotificationChannelForNotificationService()
        createReminderNotificationChannel()
        startNotificationService()
    }

    private fun startNotificationService() {
        val intent = Intent(this, NotificationService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun createReminderNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name_reminders)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(ShowNotificationsForPrayers.reminderChannelId,
                    name,
                    importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannelForNotificationService() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
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