package ba.aadil.namaz.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentDashboardBinding
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
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.getStatsBetweenDays(
            LocalDate.now().minusDays(7),
            LocalDate.now()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            dashboardViewModel.dateStats.collect {
                when (it) {
                    is DashboardViewModel.PrayingStatisticsStats.Data -> {
                        binding.prayedPrayersStats.text = getString(
                            R.string.prayed_prayers_stats, it.stats.prayedCount,
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