package ba.aadil.namaz.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ba.aadil.namaz.prayertimes.GetNextPrayerTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime


class SchedulePrayerNotificationsJob(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {
    private val getNextPrayerTime by inject<GetNextPrayerTime>()

    override suspend fun doWork(): Result {
        val (prayerTime, prayer) = getNextPrayerTime.get()
        if (prayerTime.minusMinutes(16).isBefore(LocalDateTime.now())) {
            ShowNotificationsForPrayers.show(context, prayer)
        }

        return Result.success()
    }

    companion object {
        const val jobName = "schedulePrayerNotificationsJob"
    }
}