package com.dapm.barbershop.infrastructure.ui.viewmodels.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.barbershop.application.usecases.barber.RegisterBarberUseCase
import com.dapm.barbershop.application.usecases.barber.UpdateBarberUseCase
import com.dapm.barbershop.domain.entities.Barber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberViewModel @Inject constructor(
    private val registerBarberUseCase: RegisterBarberUseCase,
    private val updateBarberUseCase: UpdateBarberUseCase
) : ViewModel()  {

    private val _updatedBarber = MutableLiveData<Result<Barber>>()
    val updatedBarber: LiveData<Result<Barber>> = _updatedBarber

    private val _registerBarber = MutableLiveData<Result<Barber>>()
    val registerBarber: LiveData<Result<Barber>> = _registerBarber

    fun registerBarber(barber: Barber) {
        viewModelScope.launch {
            try {
                registerBarberUseCase.execute(barber)
                _registerBarber.value = Result.success(barber)
            } catch (e: Exception) {
                _registerBarber.value = Result.failure(e)
            }
        }
    }

    fun updateBarber(barber: Barber) {
        viewModelScope.launch {
            try {
                updateBarberUseCase.execute(barber)
                _updatedBarber.value = Result.success(barber)
            } catch (e: Exception) {
                _updatedBarber.value = Result.failure(e)
            }
        }
    }

}