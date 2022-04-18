package ba.aadil.namaz

import ba.aadil.namaz.notifications.ShowNotificationsForPrayers
import ba.aadil.namaz.domain.PrayerEvents
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class TestReminderNotificationScheduleLogic {

    @Test
    fun testSettingAsShown() {
        assertEquals(true,
            ShowNotificationsForPrayers.shouldShowRemainderNotification(PrayerEvents.Prayers.MorningPrayer))
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.MorningPrayer, LocalDate.now())

        assertEquals(false,
            ShowNotificationsForPrayers.shouldShowRemainderNotification(PrayerEvents.Prayers.MorningPrayer))
    }

    @Test
    fun testSize() {
        // today
        val today = LocalDate.now()
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.MorningPrayer, today)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.NoonPrayer, today)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.AfterNoonPrayer, today)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.SunsetPrayer, today)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.NightPrayer, today)

        // tomorrow
        val tomorrow = LocalDate.now().plusDays(1)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.MorningPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.NoonPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.AfterNoonPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.SunsetPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.NightPrayer, tomorrow)

        // day after tomorrow
        val dayAfterTomorrow = LocalDate.now().plusDays(2)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.MorningPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.NoonPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.AfterNoonPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.SunsetPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(PrayerEvents.Prayers.NightPrayer, dayAfterTomorrow)

        assertEquals(10, ShowNotificationsForPrayers.shownRemindersSize())
    }
}