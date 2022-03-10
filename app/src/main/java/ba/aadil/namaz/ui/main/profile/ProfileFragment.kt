package ba.aadil.namaz.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentProfileBinding
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAdapter = RendererRecyclerViewAdapter()
        rvAdapter.registerRenderer(
            ViewRenderer<ProfileViewModel.Badge, ViewFinder>(
                R.layout.profile_badge,
                ProfileViewModel.Badge::class.java
            ) { model, finder, _ ->
                val text =
                    if (model.completedDays == 1)
                        getString(R.string.prayer_badge_text_one_day)
                    else
                        getString(R.string.prayer_badge_text, model.completedDays)
                finder.setText(R.id.badge_name, text)
            })
        rvAdapter.registerRenderer(
            ViewRenderer<ProfileViewModel.UnlockBadge, ViewFinder>(
                R.layout.profile_badge_unlock,
                ProfileViewModel.UnlockBadge::class.java
            ) { model, finder, _ ->
                val text = getString(R.string.prayer_unlock_badge_text, model.completedDays)
                finder.setText(R.id.badge_name, text)
            })

        binding.profileRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        val allBadgesList = listOf(1, 7, 14, 21, 28, 35, 42)
        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.badges.collect { collectedBadges ->
                val finalItems = mutableListOf<ViewModel>().apply { addAll(collectedBadges) }
                allBadgesList.forEach { days ->
                    if (collectedBadges.indexOfFirst { badge -> badge.completedDays == days } == -1) {
                        finalItems.add(ProfileViewModel.UnlockBadge(days))
                    }
                }
                rvAdapter.setItems(finalItems)
                rvAdapter.notifyDataSetChanged()
            }
        }
    }
}