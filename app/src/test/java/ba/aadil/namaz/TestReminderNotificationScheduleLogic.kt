package ba.aadil.namaz

import ba.aadil.namaz.notifications.ShowNotificationsForPrayers
import ba.aadil.namaz.prayertimes.Events
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class TestReminderNotificationScheduleLogic {

    @Test
    fun testSettingAsShown() {
        assertEquals(true,
            ShowNotificationsForPrayers.shouldShowRemainderNotification(Events.Prayers.MorningPrayer))
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.MorningPrayer, LocalDate.now())

        assertEquals(false,
            ShowNotificationsForPrayers.shouldShowRemainderNotification(Events.Prayers.MorningPrayer))
    }

    @Test
    fun testSize() {
        // today
        val today = LocalDate.now()
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.MorningPrayer, today)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.NoonPrayer, today)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.AfterNoonPrayer, today)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.SunsetPrayer, today)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.NightPrayer, today)

        // tomorrow
        val tomorrow = LocalDate.now().plusDays(1)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.MorningPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.NoonPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.AfterNoonPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.SunsetPrayer, tomorrow)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.NightPrayer, tomorrow)

        // day after tomorrow
        val dayAfterTomorrow = LocalDate.now().plusDays(2)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.MorningPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.NoonPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.AfterNoonPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.SunsetPrayer, dayAfterTomorrow)
        ShowNotificationsForPrayers.markAsShown(Events.Prayers.NightPrayer, dayAfterTomorrow)

        assertEquals(10, ShowNotificationsForPrayers.shownRemindersSize())
    }
}