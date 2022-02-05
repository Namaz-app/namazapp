package ba.aadil.namaz.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentSettingsBinding
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel by viewModel<SettingsViewModel>()

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
            ViewRenderer<NotificationsHeader, ViewFinder>(
                R.layout.notifications_header,
                NotificationsHeader::class.java
            ) { model, finder, _ ->
            })

        rvAdapter.registerRenderer(
            ViewRenderer<NotificationsRow, ViewFinder>(
                R.layout.notifications_row,
                NotificationsRow::class.java
            ) { model, finder, _ ->
                finder.setChecked(R.id.notifications_toggle, model.notifications)
                finder.setOnCheckedChangeListener(R.id.notifications_toggle) {
                    finder.find<View>(R.id.notifications_toggle).apply {
                        postDelayed({
                            settingsViewModel.toggleNotifications(it)
                        }, 400)
                    }
                }
            })

        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.reminder_times,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
        }
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        rvAdapter.registerRenderer(ViewRenderer<RemindersRow, ViewFinder>(
            R.layout.reminders_row,
            RemindersRow::class.java
        ) { model, finder, _ ->
            finder.find<Spinner>(R.id.reminders_timer).apply {
                adapter = spinnerAdapter
                onItemSelectedListener = spinnerListener
                setSelection(model.selectedIndex)
            }
        })

        rvAdapter.registerRenderer(ViewRenderer<SocialRow, ViewFinder>(
            R.layout.social_row,
            SocialRow::class.java
        ) { _, finder, _ ->
            finder.setOnClickListener(R.id.share_root) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "www.namazapp.ba")
                    type = "text/html"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        })

        binding.settingsRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.notificationOn.collect { notificationsOn ->
                rvAdapter.setItems(listOf(NotificationsHeader,
                    NotificationsRow(notificationsOn),
                    RemindersRow(selectedIndex = 2),
                    SocialRow
                ))
                rvAdapter.notifyDataSetChanged()
            }
        }
    }

    object NotificationsHeader : ViewModel
    data class NotificationsRow(val notifications: Boolean) : ViewModel
    data class RemindersRow(val selectedIndex: Int) : ViewModel
    object SocialRow : ViewModel
}