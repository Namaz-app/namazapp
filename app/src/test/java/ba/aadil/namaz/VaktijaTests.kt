package ba.aadil.namaz

import ba.aadil.namaz.vaktija.PrayerTimes
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class VaktijaTests {
    @Test
    fun addition_isCorrect() {
        val prayerTimes = PrayerTimes()
        val date = Date()
        assertEquals("12:00", prayerTimes.timeForZuhur(date))
    }
}