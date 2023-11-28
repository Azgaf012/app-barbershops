package com.dapm.barbershop.infrastructure.adapters

import com.dapm.barbershop.domain.entities.Barbershop
import com.dapm.barbershop.domain.entities.ServiceBarbershop
import com.dapm.barbershop.domain.ports.BarbershopRepository
import com.dapm.barbershop.infrastructure.dtos.ServiceBarbershopDto
import com.dapm.barbershop.infrastructure.exceptions.BarbershopException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseBarbershopRepository @Inject constructor() : BarbershopRepository {

    private val db = Firebase.firestore

    override suspend fun createBarbershop(barbershop: Barbershop) {
        try {
            db.collection("barbershops").document(barbershop.uid).set(barbershop).await()
        } catch (e: Exception) {
            throw BarbershopException("Failed to create barbershop: ${e.message}")
        }
    }

    override suspend fun retrieveBarbershops(): List<Barbershop> {
        return try {
            db.collection("barbershops").get().await().documents.mapNotNull { document ->
                document.toObject(Barbershop::class.java)
            }
        } catch (e: Exception) {
            throw BarbershopException("Failed to retrieve barbershops: ${e.message}")
        }
    }

    override suspend fun updateBarbershop(barbershop: Barbershop) {
        try {
            db.collection("barbershops").document(barbershop.uid).set(barbershop).await()
        } catch (e: Exception) {
            throw BarbershopException("Failed to update barbershop: ${e.message}")
        }
    }

    override suspend fun deleteBarbershop(uid: String) {
        try {
            val updates = hashMapOf<String, Any>(
                "state" to false
            )
            db.collection("barbershops").document(uid).update(updates).await()
        } catch (e: Exception) {
            throw BarbershopException("Failed to delete barbershop: ${e.message}")
        }
    }

    override suspend fun addServiceToBarbershop(barbershopUid: String, service: ServiceBarbershop) {
        try {
            val barbershopRef = db.collection("barbershops").document(barbershopUid)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(barbershopRef)
                val services = snapshot.get("services") as? List<Map<String, Any>> ?: emptyList()
                val newServices = services.toMutableList()
                newServices.add(ServiceBarbershopDto.fromServiceBarbershop(service).toMap())
                transaction.update(barbershopRef, "services", newServices)
            }.await()
        } catch (e: Exception) {
            throw BarbershopException("Failed to add service to barbershop: ${e.message}")
        }
    }

    override suspend fun removeServiceFromBarbershop(barbershopUid: String, serviceUid: String) {
        try {
            val barbershopRef = db.collection("barbershops").document(barbershopUid)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(barbershopRef)
                val services = snapshot.get("services") as List<HashMap<String, Any>>? ?: emptyList()
                val newServices = services.filterNot { it["uid"] == serviceUid }
                transaction.update(barbershopRef, "services", newServices)
            }.await()
        } catch (e: Exception) {
            throw BarbershopException("Failed to remove service from barbershop: ${e.message}")
        }
    }

}
