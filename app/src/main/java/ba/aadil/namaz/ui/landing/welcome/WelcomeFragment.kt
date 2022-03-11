package ba.aadil.namaz.ui.landing.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentWelcomeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WelcomeFragment: Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }
    }
}
