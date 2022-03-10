package ba.aadil.namaz.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import ba.aadil.namaz.MainActivity
import ba.aadil.namaz.R
import ba.aadil.namaz.domain.usecase.GetCurrentCityUseCase
import ba.aadil.namaz.databinding.ActivityOnboardingBinding
import ba.aadil.namaz.data.db.City
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


        val cityList = mutableListOf<City>()
        val cityNamesList = mutableListOf<String>()

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.city_picker_row, cityNamesList)
        binding.cityPicker.adapter = adapter
        binding.cityPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                cityList.getOrNull(position)?.let {
                    getCurrentCityUseCase.setCurrentCity(it._id ?: City.defaultCityId)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        lifecycle.coroutineScope.launchWhenCreated {
            val cities = withContext(Dispatchers.IO) { getCurrentCityUseCase.getAllCities() }
            cityNamesList.addAll(cities.map { it.name })
            cityList.addAll(cities)
            adapter.notifyDataSetChanged()
            binding.cityPicker.setSelection(cityNamesList.indexOfFirst { it == "Sarajevo" })
        }

        binding.finish.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}