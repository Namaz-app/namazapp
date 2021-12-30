package ba.aadil.namaz.ui.dashboard

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentDashboardBinding
import ba.aadil.namaz.db.Track
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Duration
import java.time.LocalDate

class DashboardFragment : Fragment() {
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        val yAxisFormatter = object : ValueFormatter() {
            override fun getPointLabel(entry: Entry?): String {
                return entry?.y?.toInt()?.toString() ?: "0"
            }
        }
        val xAxisFormatter = object : ValueFormatter() {
            override fun getPointLabel(entry: Entry?): String {
                return entry?.x?.toInt()?.toString() ?: "0"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            dashboardViewModel.getStatsBetweenSelectedDaysLive().collect {
                when (it) {
                    is DashboardViewModel.PrayingStatisticsStats.Data -> {
                        val fromDate = dashboardViewModel.fromDate.value.atStartOfDay()
                        val toDate =
                            dashboardViewModel.toDate.value.atStartOfDay()
                        val days = Duration.between(
                            fromDate,
                            toDate
                        ).toDays().toInt()
                        val totalPrayersMap = HashMap<String, Int>()
                        val prayedPrayersMap = HashMap<String, Int>()
                        IntRange(0, days).map { day ->
                            val date = fromDate.plusDays(day.toLong())
                            totalPrayersMap[Track.dateFormatter.format(date)] = 5
                        }
                        it.stats.trackedPrayers.forEach { track ->
                            if (track.completed) prayedPrayersMap[track.date] =
                                (prayedPrayersMap[track.date] ?: 0) + 1
                        }
                        val dataSets = arrayListOf<ILineDataSet>(
                            LineDataSet(
                                totalPrayersMap.keys.mapIndexed { index, key ->
                                    Entry(
                                        index.toFloat(),
                                        totalPrayersMap[key]?.toFloat() ?: 0f
                                    )
                                }, "Total"
                            ).apply {
                                valueFormatter = yAxisFormatter
                            },
                            LineDataSet(
                                totalPrayersMap.keys.mapIndexed { index, key ->
                                    Entry(
                                        index.toFloat(),
                                        prayedPrayersMap[key]?.toFloat() ?: 0f
                                    )
                                }, "Prayed"
                            ).apply {
                                valueFormatter = yAxisFormatter
                                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                            },
                        )
                        binding.chart.apply {
                            data = LineData(dataSets)
                            axisLeft.valueFormatter = yAxisFormatter
                            axisLeft.setDrawGridLines(false)
                            setDrawGridBackground(false)
                            xAxis.valueFormatter = xAxisFormatter
                            xAxis.setDrawGridLines(false)
                            invalidate()
                        }
                        binding.prayedPrayersStats.text = getString(
                            R.string.prayed_prayers_stats, it.stats.trackedPrayers.size,
                            it.stats.totalCount
                        )
                    }
                    is DashboardViewModel.PrayingStatisticsStats.Error -> TODO()
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