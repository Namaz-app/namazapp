package ba.aadil.namaz.ui.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ba.aadil.namaz.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}