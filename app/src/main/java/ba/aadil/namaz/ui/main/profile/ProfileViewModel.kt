package ba.aadil.namaz.ui.main.profile

import ba.aadil.namaz.data.db.BadgesDao
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.coroutines.flow.map

class ProfileViewModel(
    badgesDao: BadgesDao,
) : androidx.lifecycle.ViewModel() {
    private val _badges = badgesDao.getAllBadges().map { it.map { Badge(it.completedDays) } }
    val badges = _badges

    data class Badge(val completedDays: Int) : ViewModel
    data class UnlockBadge(val completedDays: Int) : ViewModel
}