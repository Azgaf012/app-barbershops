package com.dapm.barbershop.infrastructure.modules

import com.dapm.barbershop.domain.ports.AuthenticationRepository
import com.dapm.barbershop.domain.ports.BarberRepository
import com.dapm.barbershop.domain.ports.BarbershopRepository
import com.dapm.barbershop.infrastructure.adapters.FirebaseAuthenticationRepository
import com.dapm.barbershop.infrastructure.adapters.FirebaseBarberRepository
import com.dapm.barbershop.infrastructure.adapters.FirebaseBarbershopRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindAuthenticationRepository(
        firebaseAuthenticationRepository: FirebaseAuthenticationRepository
    ): AuthenticationRepository

    @Binds
    abstract fun bindBarbershopRepository(
        firebaseBarbershopRepository: FirebaseBarbershopRepository
    ): BarbershopRepository

    @Binds
    abstract fun bindBarberRepository(
        firebaseBarberRepository: FirebaseBarberRepository
    ): BarberRepository



}

