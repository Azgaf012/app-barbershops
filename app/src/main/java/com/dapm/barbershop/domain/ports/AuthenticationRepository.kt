package com.dapm.barbershop.domain.ports

import com.dapm.barbershop.infrastructure.dtos.UserDto
import java.time.LocalDate

interface AuthenticationRepository {
    suspend fun login(email: String, password: String): UserDto
    suspend fun register(name: String, email: String, password: String, rol: String, birthdate: LocalDate): UserDto
    fun logout()
}