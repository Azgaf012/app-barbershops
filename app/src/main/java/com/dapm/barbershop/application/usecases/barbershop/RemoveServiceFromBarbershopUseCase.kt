package com.dapm.barbershop.application.usecases.barbershop

import com.dapm.barbershop.domain.ports.BarbershopRepository
import javax.inject.Inject

class RemoveServiceFromBarbershopUseCase @Inject constructor(private val barbershopRepository: BarbershopRepository) {
    suspend fun execute(barbershopUid: String, serviceUid: String) {
        barbershopRepository.removeServiceFromBarbershop(barbershopUid, serviceUid)
    }
}