package ba.aadil.namaz.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import ba.aadil.namaz.R
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.databinding.FragmentOnboardingBinding
import ba.aadil.namaz.data.db.City
import ba.aadil.namaz.ui.landing.registration.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate

class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    val binding get() = _binding!!
    private val getCurrentCityUseCase by inject<GetCurrentCityUseCase>()
    private val registrationViewModel by sharedViewModel<RegistrationViewModel>()
    private var pickedCityId = City.defaultCityId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityList = mutableListOf<City>()
        val cityNamesList = mutableListOf<String>()

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.city_picker_row, cityNamesList)
        binding.cityPicker.adapter = adapter
        binding.cityPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                cityList.getOrNull(position)?.let {
                    it._id?.let { id ->
                        pickedCityId = id
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenCreated {
            val cities = withContext(Dispatchers.IO) { getCurrentCityUseCase.getAllCities() }
            cityNamesList.addAll(cities.map { it.name })
            cityList.addAll(cities)
            adapter.notifyDataSetChanged()
            binding.cityPicker.setSelection(cityNamesList.indexOfFirst { it == "Sarajevo" })
        }

        binding.pickBirthdateButton.apply {
            minValue = 1900
            maxValue = LocalDate.now().year
            value = 1989
        }

        binding.finish.setOnClickListener {
            registrationViewModel.completeStepTwo(pickedCityId, binding.pickBirthdateButton.value)
        }
    }
}