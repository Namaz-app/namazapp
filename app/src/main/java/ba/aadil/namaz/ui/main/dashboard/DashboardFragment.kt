package ba.aadil.namaz.ui.main.dashboard

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentDashboardBinding
import ba.aadil.namaz.ui.collectLatestLifecycleFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
        collectLatestLifecycleFlow(viewModel.prayedCount) { prayerCount ->
            binding.prayerCountText.text = getString(R.string.prayed_today_stats, prayerCount)
            binding.prayerCountIndicator.progress = when (prayerCount) {
                1 -> 20
                2 -> 40
                3 -> 60
                4 -> 80
                5 -> 100
                else -> 0
            }
        }

        collectLatestLifecycleFlow(viewModel.emojiData) {
            binding.todayEmoji.text = it.emojiText
            binding.todayMessage.text = getString(it.congratsTextId)
        }

        collectLatestLifecycleFlow(viewModel.fromDate) {
            binding.dateFrom.text = it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        }
        collectLatestLifecycleFlow(viewModel.toDate) {
            binding.dateTo.text = it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        }
        collectLatestLifecycleFlow(viewModel.selectedWeekStats) {
            it
        }


        binding.dateFrom.setOnClickListener {
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
        binding.dateTo.setOnClickListener {
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





        // val rvAdapter = RendererRecyclerViewAdapter()
        // rvAdapter.registerRenderer(
        //     ViewRenderer<GetStatisticsUseCase.SinglePrayerStats, ViewFinder>(
        //         R.layout.prayer_completion_progress,
        //         GetStatisticsUseCase.SinglePrayerStats::class.java
        //     ) { model, finder, _ ->
        //         val prayerName = when (model.type) {
        //             PrayerEvents.AfterNoonPrayer -> getString(R.string.afternoonPrayer)
        //             PrayerEvents.MorningPrayer -> getString(R.string.morningPrayer)
        //             PrayerEvents.NightPrayer -> getString(R.string.nightPrayer)
        //             PrayerEvents.NoonPrayer -> getString(R.string.noonPrayer)
        //             PrayerEvents.SunsetPrayer -> getString(R.string.sunsetPrayer)
        //             PrayerEvents.Sunrise ->getString(R.string.sunrise)
        //         }
        //         finder.setText(R.id.prayer_name, prayerName)
        //         finder.find<LinearProgressIndicator>(R.id.prayer_completion_progress).apply {
        //             max = model.totalCount
        //             progress = model.prayedCount
        //         }
        //     })

        // rvAdapter.registerRenderer(
        //     ViewRenderer<DashboardViewModel.SelectedDaysStats, ViewFinder>(
        //         R.layout.dashboard_selected_dates_stats,
        //         DashboardViewModel.SelectedDaysStats::class.java
        //     ) { model, finder, _ ->
        //         finder.setText(
        //             R.id.prayed_prayers_stats,
        //             getString(
        //                 R.string.prayed_prayers_stats,
        //                 model.prayedCount,
        //                 model.totalPrayers
        //             )
        //         )
        //     }
        // )
        //
        // rvAdapter.registerRenderer(
        //     ViewRenderer<DashboardViewModel.PrayingStatisticsStats.Data, ViewFinder>(
        //         R.layout.dashboard_stats,
        //         DashboardViewModel.PrayingStatisticsStats.Data::class.java
        //     ) { model, finder, _ ->
        //
        //         finder.setText(
        //             R.id.prayed_count,
        //             getString(R.string.prayed_today_stats, model.prayedTodayCount)
        //         )
        //         finder.setText(R.id.today_emoji, model.emoji)
        //         finder.setText(R.id.today_message, model.congratsTextId)
        //
        //         viewLifecycleOwner.lifecycleScope.launch {
        //             viewModel.fromDate.collect {
        //                 finder.find<Button>(R.id.date_from).apply {
        //                     text = it.format(PrayerTrackingInfo.dateFormatter)
        //                 }
        //             }
        //         }
        //
        //         viewLifecycleOwner.lifecycleScope.launch {
        //             viewModel.toDate.collect {
        //                 finder.find<Button>(R.id.date_to).apply {
        //                     text = it.format(PrayerTrackingInfo.dateFormatter)
        //                 }
        //             }
        //         }
        //
        //         finder.find<Button>(R.id.date_from).apply {
        //             setOnClickListener {
        //                 context?.let {
        //                     DatePickerDialog(
        //                         it,
        //                         { _, year, month, day ->
        //                             val fromLocalDate =
        //                                 LocalDate.now().withYear(year).withMonth(month + 1)
        //                                     .withDayOfMonth(day)
        //                             viewModel.updateFromDate(fromLocalDate)
        //                         },
        //                         viewModel.fromDate.value.year,
        //                         viewModel.fromDate.value.monthValue - 1,
        //                         viewModel.fromDate.value.dayOfMonth
        //                     ).show()
        //                 }
        //             }
        //         }
        //
        //         finder.find<Button>(R.id.date_to).apply {
        //             setOnClickListener {
        //                 context?.let {
        //                     DatePickerDialog(
        //                         it,
        //                         { _, year, month, day ->
        //                             val toLocalDate =
        //                                 LocalDate.now().withYear(year).withMonth(month + 1)
        //                                     .withDayOfMonth(day)
        //                             viewModel.updateToDate(toLocalDate)
        //                         },
        //                         viewModel.toDate.value.year,
        //                         viewModel.toDate.value.monthValue - 1,
        //                         viewModel.toDate.value.dayOfMonth
        //                     ).show()
        //                 }
        //             }
        //         }
        //     }
        // )

        // viewLifecycleOwner.lifecycleScope.launch {
        //
        //     viewModel.getStatsBetweenSelectedDaysLive().collect {
        //         when (it) {
        //             is DashboardViewModel.PrayingStatisticsStats.Data -> {
        //                 val items = mutableListOf<ViewModel>(it)
        //                 items.addAll(it.data.stats.sortedBy { prayerStats -> prayerStats.sortWeight })
        //                 items.add(it.selectedDaysStats)
        //                 rvAdapter.setItems(items)
        //             }
        //             is DashboardViewModel.PrayingStatisticsStats.Error -> {
        //             }
        //             DashboardViewModel.PrayingStatisticsStats.Loading -> {
        //             }
        //         }
        //     }
        // }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
