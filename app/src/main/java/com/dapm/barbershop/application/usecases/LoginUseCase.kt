package com.dapm.barbershop.application.usecases

import com.dapm.barbershop.domain.ports.AuthenticationRepository
import com.dapm.barbershop.infrastructure.dtos.UserDto
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {
    suspend fun execute(email: String, password: String): UserDto {
        return authenticationRepository.login(email, password)
    }
}