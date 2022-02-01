package ba.aadil.namaz.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentSettingsBinding
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAdapter = RendererRecyclerViewAdapter()
        rvAdapter.registerRenderer(
            ViewRenderer<NotificationsRow, ViewFinder>(
                R.layout.notifications_row,
                NotificationsRow::class.java
            ) { model, finder, _ ->
                finder.setChecked(R.id.notifications_toggle, model.notifications)
            })

        binding.settingsRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        rvAdapter.setItems(listOf(NotificationsRow(true)))
        rvAdapter.notifyDataSetChanged()
    }

    data class NotificationsRow(val notifications: Boolean) : ViewModel
    data class RemindersRow(val reminderTime: Int) : ViewModel
    object SocialsRow : ViewModel
}