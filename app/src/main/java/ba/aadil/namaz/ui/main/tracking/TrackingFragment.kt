package ba.aadil.namaz.ui.main.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentTrackingBinding
import ba.aadil.namaz.domain.Events
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TrackingFragment : Fragment() {

    private val trackingViewModel: TrackingViewModel by inject()
    private var _binding: FragmentTrackingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAdapter = RendererRecyclerViewAdapter()
        rvAdapter.registerRenderer(
            ViewRenderer<TrackingUIModel, ViewFinder>(
                R.layout.tracking_layout,
                TrackingUIModel::class.java
            ) {
                    model,
                    finder,
                    _,
                ->
                finder.setText(R.id.prayer_name, model.prayerName)
                finder.setChecked(R.id.prayer_tracking_checkbox, model.track)
                finder.setOnCheckedChangeListener(R.id.prayer_tracking_checkbox) {
                    trackingViewModel.markAsPrayed(model.prayer, it)
                }
            })

        rvAdapter.registerRenderer(
            ViewRenderer<TrackingHeader, ViewFinder>(
                R.layout.tracking_header_layout,
                TrackingHeader::class.java
            ) { model, finder, _ ->
                finder.setText(R.id.user_name,
                    getString(R.string.welcome_user, model.userName))
                finder.setText(R.id.date, model.date)
                finder.setText(R.id.prayed_count,
                    getString(R.string.prayed_today_stats, model.prayerCount))
            })

        binding.prayersRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            trackingViewModel.state.collect {
                when (it) {
                    is TrackingViewModel.TrackingPrayersState.Data -> {
                        rvAdapter.setItems(it.list)
                        rvAdapter.notifyDataSetChanged()
                    }
                    is TrackingViewModel.TrackingPrayersState.Error -> {
                    }
                    TrackingViewModel.TrackingPrayersState.Loading -> {

                    }
                }
            }
        }

        trackingViewModel.getTracking()
    }

    class TrackingUIModel(val prayerName: String, val track: Boolean, val prayer: Events.Prayers) :
        ViewModel

    class TrackingHeader(val userName: String, val prayerCount: Int, val date: String) :
        ViewModel

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}