package ba.aadil.namaz.notifications

import android.content.Context
import androidx.work.*
import ba.aadil.namaz.prayertimes.GetNextPrayerTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class SchedulePrayerNotificationsJob(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {
    private val getNextPrayerTime by inject<GetNextPrayerTime>()

    override suspend fun doWork(): Result {
        val (prayerTime, _) = getNextPrayerTime.get()
        val showNotificationJob =
            OneTimeWorkRequestBuilder<ShowPrayerNotificationsJob>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInitialDelay(
                    prayerTime.minusMinutes(15)
                        .toEpochSecond(ZoneOffset.ofTotalSeconds(0)) -
                            LocalDateTime.now()
                                .toEpochSecond(ZoneOffset.ofTotalSeconds(0)),
                    TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(ShowPrayerNotificationsJob.jobName,
                ExistingWorkPolicy.KEEP,
                showNotificationJob)
        return Result.success()
    }

    companion object {
        const val jobName = "schedulePrayerNotificationsJob"
    }
}