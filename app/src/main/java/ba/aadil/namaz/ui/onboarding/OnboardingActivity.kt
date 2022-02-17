package ba.aadil.namaz.ui.onboarding

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import ba.aadil.namaz.city.GetCurrentCityUseCase
import ba.aadil.namaz.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    private val getCurrentCityUseCase by inject<GetCurrentCityUseCase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val cityList = mutableListOf<String>()

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList)
        binding.cityPicker.adapter = adapter

        lifecycle.coroutineScope.launchWhenCreated {
            val cities = withContext(Dispatchers.IO) { getCurrentCityUseCase.getAllCities() }
            cityList.addAll(cities.map { it.name })
            adapter.notifyDataSetChanged()
        }
    }
}