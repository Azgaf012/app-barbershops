package com.dapm.barbershop.infrastructure.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dapm.barbershop.R
import com.dapm.barbershop.databinding.FragmentLoginBinding
import com.dapm.barbershop.domain.enums.EnumRol
import com.dapm.barbershop.infrastructure.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            onLoginButtonClicked()
        }

        binding.signUpLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.loginResult.collect { userDto ->
                if (userDto != null) {
                    Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                    when (userDto.rol) {
                        EnumRol.ADMIN.name -> findNavController().navigate(R.id.action_loginFragment_to_adminMenuFragment)
                        EnumRol.CUSTOMER.name -> findNavController().navigate(R.id.action_loginFragment_to_customerMenuFragment)
                        else -> {

                        }
                    }
                } else {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onLoginButtonClicked() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        loginViewModel.login(email, password)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}