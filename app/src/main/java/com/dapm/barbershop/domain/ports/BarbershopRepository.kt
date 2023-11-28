package com.dapm.barbershop.domain.ports

import com.dapm.barbershop.domain.entities.Barbershop
import com.dapm.barbershop.domain.entities.ServiceBarbershop

interface BarbershopRepository {
    suspend fun createBarbershop(barbershop: Barbershop)
    suspend fun retrieveBarbershops(): List<Barbershop>
    suspend fun updateBarbershop(barbershop: Barbershop)
    suspend fun deleteBarbershop(uid: String)
    suspend fun addServiceToBarbershop(barbershopUid: String, service: ServiceBarbershop)
    suspend fun removeServiceFromBarbershop(barbershopUid: String, serviceUid: String)
}