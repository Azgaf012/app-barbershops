package com.dapm.barbershop.domain.ports

import com.dapm.barbershop.domain.entities.Barber

interface BarberRepository {
    suspend fun createBarber(barber: Barber)
    suspend fun retrieveBarbers(): List<Barber>
    suspend fun updateBarber(barber: Barber)
}