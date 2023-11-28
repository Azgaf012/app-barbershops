package com.dapm.barbershop.infrastructure.ui.viewmodels.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.barbershop.application.usecases.barber.RetrieveBarberUseCase
import com.dapm.barbershop.application.usecases.barber.UpdateBarberUseCase
import com.dapm.barbershop.domain.entities.Barber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberListViewModel @Inject constructor(
    private val retrieveBarberUseCase: RetrieveBarberUseCase,
    private val updateBarberUseCase: UpdateBarberUseCase
) : ViewModel()  {

    private val _barberList = MutableLiveData<List<Barber>>()

    val barberList: LiveData<List<Barber>> get() = _barberList

    private val _updatedBarber = MutableLiveData<Result<Barber>>()
    val updatedBarber: LiveData<Result<Barber>> get() = _updatedBarber

    fun fetchBarbers() {
        viewModelScope.launch {
            try {
                val barbers = retrieveBarberUseCase.execute()
                _barberList.value = barbers
            } catch (e: Exception) {

            }
        }
    }

    suspend fun updateBarber(barber: Barber) {
        viewModelScope.launch {
            try {
                updateBarberUseCase.execute(barber)
                _updatedBarber.value = Result.success(barber)
            } catch (e: Exception) {

            }
        }
    }

}