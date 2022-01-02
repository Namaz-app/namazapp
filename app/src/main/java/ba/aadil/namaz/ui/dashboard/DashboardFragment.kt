package ba.aadil.namaz.ui.dashboard

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentDashboardBinding
import ba.aadil.namaz.db.Track
import ba.aadil.namaz.prayertimes.Events
import ba.aadil.namaz.stats.GetStatisticsUseCase
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class DashboardFragment : Fragment() {
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateFrom.setOnClickListener {
            context?.let {
                DatePickerDialog(
                    it,
                    { _, year, month, day ->
                        val fromLocalDate =
                            LocalDate.now().withYear(year).withMonth(month + 1).withDayOfMonth(day)
                        dashboardViewModel.updateFromDate(fromLocalDate)
                    },
                    dashboardViewModel.fromDate.value.year,
                    dashboardViewModel.fromDate.value.monthValue - 1,
                    dashboardViewModel.fromDate.value.dayOfMonth
                ).show()
            }
        }

        binding.dateTo.setOnClickListener {
            context?.let {
                DatePickerDialog(
                    it,
                    { _, year, month, day ->
                        val toLocalDate =
                            LocalDate.now().withYear(year).withMonth(month + 1).withDayOfMonth(day)
                        dashboardViewModel.updateToDate(toLocalDate)
                    },
                    dashboardViewModel.toDate.value.year,
                    dashboardViewModel.toDate.value.monthValue - 1,
                    dashboardViewModel.toDate.value.dayOfMonth
                ).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            dashboardViewModel.fromDate.collect {
                binding.dateFrom.text = it.format(Track.dateFormatter)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            dashboardViewModel.toDate.collect {
                binding.dateTo.text = it.format(Track.dateFormatter)
            }
        }

        val rvAdapter = RendererRecyclerViewAdapter()
        rvAdapter.registerRenderer(
            ViewRenderer<GetStatisticsUseCase.SinglePrayerStats, ViewFinder>(
                R.layout.prayer_completion_progress,
                GetStatisticsUseCase.SinglePrayerStats::class.java
            ) { model, finder, _ ->
                val prayerName = when (model.type) {
                    Events.Prayers.AfterNoonPrayer -> getString(R.string.afternoonPrayer)
                    Events.Prayers.MorningPrayer -> getString(R.string.morningPrayer)
                    Events.Prayers.NightPrayer -> getString(R.string.nightPrayer)
                    Events.Prayers.NoonPrayer -> getString(R.string.noonPrayer)
                    Events.Prayers.SunsetPrayer -> getString(R.string.sunsetPrayer)
                }
                finder.setText(R.id.prayer_name, prayerName)
                finder.find<LinearProgressIndicator>(R.id.prayer_completion_progress).apply {
                    max = model.totalCount
                    progress = model.prayedCount
                }
            })

        binding.prayerCompletionList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            dashboardViewModel.getStatsBetweenSelectedDaysLive().collect {
                when (it) {
                    is DashboardViewModel.PrayingStatisticsStats.Data -> {
                        rvAdapter.setItems(it.stats.states.sortedBy { prayerStats -> prayerStats.sortWeight })
                        rvAdapter.notifyDataSetChanged()
                        binding.prayedPrayersStats.text = getString(
                            R.string.prayed_prayers_stats, it.stats.trackedPrayers.size,
                            it.stats.totalCount
                        )
                    }
                    is DashboardViewModel.PrayingStatisticsStats.Error -> {
                    }
                    DashboardViewModel.PrayingStatisticsStats.Loading -> {
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}