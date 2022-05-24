package ba.aadil.namaz

import ba.aadil.namaz.notifications.ShowNotificationsForPrayers
import ba.aadil.namaz.domain.PrayerEvent
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class TestReminderNotificationScheduleLogic {

    @Test
    fun testSettingAsShown() {
        assertEquals(true,
            ShowNotificationsForPrayers.shouldShowRemainderNotification(PrayerEvent.Prayers.MorningPrayer))
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.MorningPrayer, LocalDate.now())

        assertEquals(false,
            ShowNotificationsForPrayers.shouldShowRemainderNotification(PrayerEvent.Prayers.MorningPrayer))
    }

    @Test
    fun testSize() {
        // today
        val today = LocalDate.now()
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.MorningPrayer, today)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.NoonPrayer, today)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.AfterNoonPrayer, today)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.SunsetPrayer, today)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.NightPrayer, today)

        // tomorrow
        val tomorrow = LocalDate.now().plusDays(1)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.MorningPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.NoonPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.AfterNoonPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.SunsetPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.NightPrayer, tomorrow)

        // day after tomorrow
        val dayAfterTomorrow = LocalDate.now().plusDays(2)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.MorningPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.NoonPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.AfterNoonPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.SunsetPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvent.Prayers.NightPrayer, dayAfterTomorrow)

        assertEquals(10, ShowNotificationsForPrayers.shownRemindersSize())
    }
}