package com.dapm.barbershop.application.usecases.barber

import com.dapm.barbershop.domain.entities.Barber
import com.dapm.barbershop.domain.ports.BarberRepository
import javax.inject.Inject

class RetrieveBarberUseCase @Inject constructor(private val barberRepository: BarberRepository) {

    suspend fun execute(): List<Barber> {
        return barberRepository.retrieveBarbers()
    }

}