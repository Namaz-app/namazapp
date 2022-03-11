package ba.aadil.namaz.ui.landing.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentLoginBinding
import ba.aadil.namaz.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener {
            if (fieldsValid()) {
                viewModel.login(
                    binding.emailEdittext.text.toString(),
                    binding.passwordEdittext.text.toString()
                )
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    LoginViewModel.Event.NavigateToHome -> {
                        startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    }
                    is LoginViewModel.Event.ShowError -> Snackbar.make(
                        binding.login,
                        it.message,
                        Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun fieldsValid(): Boolean {
        var hasErrors = false
        if (Patterns.EMAIL_ADDRESS.matcher(binding.emailEdittext.text.toString()).matches()) {
            binding.email.error = null
        } else {
            binding.email.error = getString(R.string.email_not_valid)
            hasErrors = true
        }
        binding.passwordEdittext.text.toString().let {
            when {
                it.isBlank() -> {
                    binding.password.error = getString(R.string.empty_field)
                    hasErrors = true
                }
                else -> {
                    binding.password.error = null
                }
            }
        }
        return !hasErrors
    }
}