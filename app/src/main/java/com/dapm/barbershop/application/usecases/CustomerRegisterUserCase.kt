package com.dapm.barbershop.application.usecases

import com.dapm.barbershop.domain.enums.EnumRol
import com.dapm.barbershop.domain.ports.AuthenticationRepository
import com.dapm.barbershop.infrastructure.dtos.UserDto
import java.time.LocalDate
import javax.inject.Inject

class CustomerRegisterUserCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {

    suspend fun execute(name: String, email: String, password: String, birthdate: LocalDate): UserDto {
        return authenticationRepository.register(name, email, password, EnumRol.CUSTOMER.name, birthdate)
    }

}
