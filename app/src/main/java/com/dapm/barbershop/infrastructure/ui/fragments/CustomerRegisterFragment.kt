package com.dapm.barbershop.infrastructure.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dapm.barbershop.R
import com.dapm.barbershop.databinding.FragmentRegisterBinding
import com.dapm.barbershop.infrastructure.ui.viewmodels.CustomerRegisterViewModel
import com.dapm.barbershop.infrastructure.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerRegisterFragment : Fragment() {

    private val customerRegisterViewModel: CustomerRegisterViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val birthdate = binding.editTextBirthdate.text.toString()
            customerRegisterViewModel.registerUser(name, email, password, birthdate)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            customerRegisterViewModel.registerResult.collect { userDto ->
                sharedViewModel.updateUser(userDto)
                if (userDto != null) {
                    Toast.makeText(context, "Registration success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            customerRegisterViewModel.errorMessage.collect { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomerRegisterFragment()
    }
}
