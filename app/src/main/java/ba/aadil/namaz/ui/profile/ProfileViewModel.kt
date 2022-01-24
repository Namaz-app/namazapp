package ba.aadil.namaz.ui.profile

import androidx.lifecycle.viewModelScope
import ba.aadil.namaz.user.GetBadges
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getBadges: GetBadges,
) : androidx.lifecycle.ViewModel() {
    private val _badges = MutableStateFlow(listOf<Badge>())
    val badges = _badges.asStateFlow()

    fun getUserBadges() {
        viewModelScope.launch(Dispatchers.IO) {
            _badges.value = getBadges.getAllBadges().map {
                Badge(it.days)
            }
        }
    }

    data class Badge(val completedDays: Int) : ViewModel
}