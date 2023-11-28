package com.dapm.barbershop.infrastructure.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.barbershop.application.usecases.LoginUseCase
import com.dapm.barbershop.infrastructure.dtos.UserDto
import com.google.firebase.events.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _loginResult = MutableSharedFlow<UserDto?>()
    val loginResult: Flow<UserDto?> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = loginUseCase.execute(email, password)
            _loginResult.emit(result)
        }
    }
}