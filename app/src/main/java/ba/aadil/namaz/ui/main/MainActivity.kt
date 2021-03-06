package ba.aadil.namaz.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.ActivityMainBinding
import ba.aadil.namaz.domain.Events
import ba.aadil.namaz.ui.landing.LandingActivity
import ba.aadil.namaz.ui.main.tracking.TrackingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val trackingViewModel by viewModel<TrackingViewModel>()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val prayerId = intent?.getIntExtra("prayer", 1) ?: 1
        trackingViewModel.markAsPrayed(Events.Prayers.fromSortWeight(prayerId), true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LandingActivity::class.java))
            finish()
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val navView: BottomNavigationView = binding.navView
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_prayer_tab,
                    R.id.navigation_dashboard_tab,
                    R.id.navigation_tracking_tab,
                    R.id.profileFragment,
                    R.id.settingsFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }
}