package ba.aadil.namaz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentHomeBinding
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import kotlinx.coroutines.flow.collect
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
        savedInstanceState: Bundle?
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
            ) { model,
                finder,
                _ ->
                finder.setText(R.id.prayer_name, model.name)
                finder.setText(R.id.prayer_time, model.time)
            })

        binding.prayersRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.prayersSchedule.filterNotNull().collect {
                rvAdapter.setItems(
                    listOf(
                        PrayerUIModel(name = "Sabah", it.morningPrayer),
                        PrayerUIModel(name = "Izlaz sunca", it.sunrise),
                        PrayerUIModel(name = "Podne", it.noonPrayer),
                        PrayerUIModel(name = "Ikinda", it.afterNoonPrayer),
                        PrayerUIModel(name = "Aksam", it.sunsetPrayer),
                        PrayerUIModel(name = "Jacija", it.nightPrayer)
                    )
                )
            }
        }

        homeViewModel.getPrayersSchedule()
    }

    class PrayerUIModel(val name: String, val time: String) : ViewModel

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}