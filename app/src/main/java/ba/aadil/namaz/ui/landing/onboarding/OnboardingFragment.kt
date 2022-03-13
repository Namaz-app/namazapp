package ba.aadil.namaz.ui.landing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ba.aadil.namaz.R
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.databinding.FragmentOnboardingBinding
import ba.aadil.namaz.data.db.City
import ba.aadil.namaz.ui.landing.onboarding.OnboardingViewModel
import ba.aadil.namaz.ui.landing.registration.RegistrationViewModel
import ba.aadil.namaz.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    val binding get() = _binding!!
    private val viewModel by viewModel<OnboardingViewModel>()
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
        lifecycleScope.launchWhenStarted {

            viewModel.cities.collect {
                cityNamesList.addAll(it.map { it.name })
                cityList.addAll(it)
                adapter.notifyDataSetChanged()
                binding.cityPicker.setSelection(cityNamesList.indexOfFirst { it == "Sarajevo" })
            }

        }
        lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    OnboardingViewModel.Event.NavigateToHome -> {
                        startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    }
                }
            }
        }
        binding.pickBirthdateButton.apply {
            minValue = 1900
            maxValue = LocalDate.now().year
            value = 1989
        }

        binding.finish.setOnClickListener {
            viewModel.updateUserInfo(pickedCityId, binding.pickBirthdateButton.value)
        }
    }
}