package com.dapm.barbershop.application.usecases.barbershop

import com.dapm.barbershop.domain.entities.Barbershop
import com.dapm.barbershop.domain.ports.BarbershopRepository
import javax.inject.Inject

class RegisterBarbershopUseCase @Inject constructor(private val barbershopRepository: BarbershopRepository) {
    suspend fun execute(barbershop: Barbershop) {
        barbershopRepository.createBarbershop(barbershop)
    }
}