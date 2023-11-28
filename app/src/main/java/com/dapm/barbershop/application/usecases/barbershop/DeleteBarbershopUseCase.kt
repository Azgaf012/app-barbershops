package com.dapm.barbershop.application.usecases.barbershop

import com.dapm.barbershop.domain.ports.BarbershopRepository
import javax.inject.Inject

class DeleteBarbershopUseCase @Inject constructor(private val barbershopRepository: BarbershopRepository) {
    suspend fun execute(uid: String) {
        barbershopRepository.deleteBarbershop(uid)
    }
}