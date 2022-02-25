package ba.aadil.namaz.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ba.aadil.namaz.R
import ba.aadil.namaz.databinding.FragmentRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val registrationViewModel by sharedViewModel<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next.setOnClickListener {
            if (fieldsValid()) {
                registrationViewModel.completeStepOne(binding.emailEdittext.text.toString(),
                    binding.passwordEdittext.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            registrationViewModel.errors.collect {
                Snackbar.make(binding.root,
                    getString(R.string.error_creating_user, it),
                    Snackbar.LENGTH_LONG).show()
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
        binding.nameEdittext.text.toString().let {
            when {
                it.isBlank() -> {
                    binding.name.error = getString(R.string.empty_field)
                    hasErrors = true
                }
                it.length < 3 -> {
                    binding.name.error = getString(R.string.minimum_three_characters)
                    hasErrors = true
                }
                else -> {
                    binding.name.error = null
                }
            }
        }
        binding.passwordEdittext.text.toString().let {
            when {
                it.isBlank() -> {
                    binding.password.error = getString(R.string.empty_field)
                    hasErrors = true
                }
                it.length < 3 -> {
                    binding.password.error = getString(R.string.minimum_three_characters)
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