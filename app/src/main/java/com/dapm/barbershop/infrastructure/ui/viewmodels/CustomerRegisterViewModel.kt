package com.dapm.barbershop.infrastructure.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.barbershop.application.usecases.CustomerRegisterUserCase
import com.dapm.barbershop.infrastructure.dtos.UserDto
import com.dapm.barbershop.infrastructure.exceptions.AuthenticationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CustomerRegisterViewModel @Inject constructor(
    private val registerUseCase: CustomerRegisterUserCase
) : ViewModel() {

    private val _registerResult = MutableSharedFlow<UserDto?>()
    val registerResult: SharedFlow<UserDto?> get() = _registerResult

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> get() = _errorMessage

    fun registerUser(name: String, email: String, password: String, birthdate: String) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val date = LocalDate.parse(birthdate, formatter)
            try {
                val result = registerUseCase.execute(name, email, password, date)
                _registerResult.emit(result)
            } catch (e: AuthenticationException) {
                _errorMessage.emit(e.message ?: "Unknown error")
            }
        }
    }
}
