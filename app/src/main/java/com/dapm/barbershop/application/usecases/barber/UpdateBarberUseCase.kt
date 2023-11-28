package com.dapm.barbershop.application.usecases.barber

import com.dapm.barbershop.domain.entities.Barber
import com.dapm.barbershop.domain.ports.BarberRepository
import javax.inject.Inject

class UpdateBarberUseCase @Inject constructor(private val barberRepository: BarberRepository) {

    suspend fun execute(barber: Barber) {
        barberRepository.updateBarber(barber)
    }
}