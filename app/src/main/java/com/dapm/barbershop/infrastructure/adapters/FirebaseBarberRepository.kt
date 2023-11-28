package com.dapm.barbershop.infrastructure.adapters

import com.dapm.barbershop.domain.entities.Barber
import com.dapm.barbershop.domain.ports.BarberRepository
import com.dapm.barbershop.infrastructure.exceptions.BarbershopException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirebaseBarberRepository @Inject constructor() : BarberRepository {

    private val db = Firebase.firestore

    override suspend fun createBarber(barber: Barber) {
        try {
            barber.uid = UUID.randomUUID().toString()
            db.collection("barbers").document(barber.uid).set(barber).await()
        } catch (e: Exception) {
            throw BarbershopException("Failed to create barber: ${e.message}")
        }
    }

    override suspend fun retrieveBarbers(): List<Barber> {
        return try {
            db.collection("barbers").get().await().documents.mapNotNull { document ->
                document.toObject(Barber::class.java)
            }
        } catch (e: Exception) {
            throw BarbershopException("Failed to retrieve barbers: ${e.message}")
        }
    }

    override suspend fun updateBarber(barber: Barber) {
        try {
            db.collection("barbers").document(barber.uid).set(barber).await()
        } catch (e: Exception) {
            throw BarbershopException("Failed to update barber: ${e.message}")
        }
    }
}