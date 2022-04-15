package ba.aadil.namaz.ui.main.dashboard

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ba.aadil.namaz.R
import ba.aadil.namaz.data.db.Track
import ba.aadil.namaz.databinding.FragmentDashboardBinding
import ba.aadil.namaz.domain.Events
import ba.aadil.namaz.domain.usecase.GetStatisticsUseCase
import ba.aadil.namaz.ui.collectLatestLifecycleFlow
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by viewModel()
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

        collectLatestLifecycleFlow(viewModel.userName) {
            binding.userName.text = getString(R.string.welcome_user, it)
        }
        collectLatestLifecycleFlow(viewModel.todayDate) {
            binding.dateToday.text =
                it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        }
        collectLatestLifecycleFlow(viewModel.prayedCount) {
            binding.prayerCount.text = getString(R.string.prayed_today_stats, it)

        }



        binding.userName.setOnClickListener {
            print(10)
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

        rvAdapter.registerRenderer(
            ViewRenderer<DashboardViewModel.SelectedDaysStats, ViewFinder>(
                R.layout.dashboard_selected_dates_stats,
                DashboardViewModel.SelectedDaysStats::class.java
            ) { model, finder, _ ->
                finder.setText(
                    R.id.prayed_prayers_stats,
                    getString(
                        R.string.prayed_prayers_stats,
                        model.prayedCount,
                        model.totalPrayers
                    )
                )
            }
        )

        rvAdapter.registerRenderer(
            ViewRenderer<DashboardViewModel.PrayingStatisticsStats.Data, ViewFinder>(
                R.layout.dashboard_stats,
                DashboardViewModel.PrayingStatisticsStats.Data::class.java
            ) { model, finder, _ ->

                finder.setText(
                    R.id.prayed_count,
                    getString(R.string.prayed_today_stats, model.prayedTodayCount)
                )
                finder.setText(R.id.today_emoji, model.emoji)
                finder.setText(R.id.today_message, model.congratsTextId)

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.fromDate.collect {
                        finder.find<Button>(R.id.date_from).apply {
                            text = it.format(Track.dateFormatter)
                        }
                    }
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.toDate.collect {
                        finder.find<Button>(R.id.date_to).apply {
                            text = it.format(Track.dateFormatter)
                        }
                    }
                }

                finder.find<Button>(R.id.date_from).apply {
                    setOnClickListener {
                        context?.let {
                            DatePickerDialog(
                                it,
                                { _, year, month, day ->
                                    val fromLocalDate =
                                        LocalDate.now().withYear(year).withMonth(month + 1)
                                            .withDayOfMonth(day)
                                    viewModel.updateFromDate(fromLocalDate)
                                },
                                viewModel.fromDate.value.year,
                                viewModel.fromDate.value.monthValue - 1,
                                viewModel.fromDate.value.dayOfMonth
                            ).show()
                        }
                    }
                }

                finder.find<Button>(R.id.date_to).apply {
                    setOnClickListener {
                        context?.let {
                            DatePickerDialog(
                                it,
                                { _, year, month, day ->
                                    val toLocalDate =
                                        LocalDate.now().withYear(year).withMonth(month + 1)
                                            .withDayOfMonth(day)
                                    viewModel.updateToDate(toLocalDate)
                                },
                                viewModel.toDate.value.year,
                                viewModel.toDate.value.monthValue - 1,
                                viewModel.toDate.value.dayOfMonth
                            ).show()
                        }
                    }
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getStatsBetweenSelectedDaysLive().collect {
                when (it) {
                    is DashboardViewModel.PrayingStatisticsStats.Data -> {
                        val items = mutableListOf<ViewModel>(it)
                        items.addAll(it.data.stats.sortedBy { prayerStats -> prayerStats.sortWeight })
                        items.add(it.selectedDaysStats)
                        rvAdapter.setItems(items)
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
