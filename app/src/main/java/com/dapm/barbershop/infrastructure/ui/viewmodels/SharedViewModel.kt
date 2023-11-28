package com.dapm.barbershop.infrastructure.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dapm.barbershop.domain.entities.Barber
import com.dapm.barbershop.domain.entities.Barbershop
import com.dapm.barbershop.infrastructure.dtos.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _currentUser = MutableLiveData<UserDto?>()
    val currentUser: LiveData<UserDto?> get() = _currentUser
    val barberSelected = MutableLiveData<Barber>()
    val barbershopSelected = MutableLiveData<Barbershop>()

    fun updateUser(user: UserDto?) {
        _currentUser.value = user
    }
}