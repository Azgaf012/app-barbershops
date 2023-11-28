package com.dapm.barbershop.infrastructure.ui.viewmodels.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.barbershop.application.usecases.barbershop.RetrieveBarbershopsUseCase
import com.dapm.barbershop.application.usecases.barbershop.UpdateBarbershopUseCase
import com.dapm.barbershop.domain.entities.Barber
import com.dapm.barbershop.domain.entities.Barbershop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarbershopListViewModel @Inject constructor(
    private val retrieveBarbershopsUseCase: RetrieveBarbershopsUseCase,
    private val updateBarbershopUseCase: UpdateBarbershopUseCase
) : ViewModel()  {

    private val _barbershopsList = MutableLiveData<List<Barbershop>>()

    val barbershopList: LiveData<List<Barbershop>> get() = _barbershopsList

    private val _updatedBarbershop = MutableLiveData<Result<Barbershop>>()
    val updatedBarbershop: LiveData<Result<Barbershop>> get() = _updatedBarbershop

    fun fetchBarbershops() {
        viewModelScope.launch {
            try {
                val barbershops = retrieveBarbershopsUseCase.execute()
                _barbershopsList.value = barbershops
            } catch (e: Exception) {

            }
        }
    }

    suspend fun updateBarbershop(barbershop: Barbershop) {
        viewModelScope.launch {
            try {
                updateBarbershopUseCase.execute(barbershop)
                _updatedBarbershop.value = Result.success(barbershop)
            } catch (e: Exception) {

            }
        }
    }

}