package ba.aadil.namaz.ui.main.vaktija

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ba.aadil.namaz.R
import ba.aadil.namaz.domain.usecase.GetCurrentDateTimeAndCity
import ba.aadil.namaz.databinding.FragmentHomeBinding
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by inject()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAdapter = RendererRecyclerViewAdapter()
        rvAdapter.registerRenderer(
            ViewRenderer<PrayerUIModel, ViewFinder>(
                R.layout.prayer_layout,
                PrayerUIModel::class.java
            ) {
                    model,
                    finder,
                    _,
                ->
                finder.setText(R.id.prayer_name, model.name)
                finder.setText(R.id.prayer_time, model.time)
            })

        rvAdapter.registerRenderer(
            ViewRenderer<CurrentCityTimeTillNext, ViewFinder>(
                R.layout.date_time_city_layout,
                CurrentCityTimeTillNext::class.java
            ) { model, finder, _ ->
                finder.setText(R.id.current_city, model.dateTimeCity.city)
                finder.setText(R.id.current_time, model.dateTimeCity.time)
                finder.setText(R.id.till_next_prayer, model.timeTillNextPrayer)
                finder.setText(R.id.current_date, model.dateTimeCity.date)
            })

        binding.prayersRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.data.filterNotNull()
                .collect { (vaktijaModel, dateTimeCity, timeTillNextPrayer) ->
                    rvAdapter.setItems(
                        listOf(
                            CurrentCityTimeTillNext(dateTimeCity, timeTillNextPrayer),
                            PrayerUIModel(name = getString(R.string.dawn),
                                vaktijaModel.morningPrayer),
                            PrayerUIModel(name = getString(R.string.sunrise), vaktijaModel.sunrise),
                            PrayerUIModel(name = getString(R.string.noonPrayer),
                                vaktijaModel.noonPrayer),
                            PrayerUIModel(
                                name = getString(R.string.afternoonPrayer),
                                vaktijaModel.afterNoonPrayer
                            ),
                            PrayerUIModel(name = getString(R.string.sunsetPrayer),
                                vaktijaModel.sunsetPrayer),
                            PrayerUIModel(name = getString(R.string.nightPrayer),
                                vaktijaModel.nightPrayer)
                        )
                    )
                    rvAdapter.notifyDataSetChanged()
                }
        }

        homeViewModel.getPrayersSchedule()
    }

    data class PrayerUIModel(val name: String, val time: String) : ViewModel
    data class CurrentCityTimeTillNext(
        val dateTimeCity: GetCurrentDateTimeAndCity.Data,
        val timeTillNextPrayer: String,
    ) : ViewModel

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}