package com.dapm.barbershop.application.usecases.barbershop

import com.dapm.barbershop.domain.entities.ServiceBarbershop
import com.dapm.barbershop.domain.ports.BarbershopRepository
import javax.inject.Inject

class AddServiceToBarbershopUseCase @Inject constructor(private val barbershopRepository: BarbershopRepository) {
    suspend fun execute(barbershopUid: String, service: ServiceBarbershop) {
        barbershopRepository.addServiceToBarbershop(barbershopUid, service)
    }
}