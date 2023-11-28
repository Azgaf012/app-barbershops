package com.dapm.barbershop.domain.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val rol: String,
    val birthdate: Timestamp
) {
    companion object {
        fun fromDocument(snapshot: DocumentSnapshot): User? {
            return try {
                val uid = snapshot.id
                val name = snapshot.getString("name") ?: return null
                val email = snapshot.getString("email") ?: return null
                val rol = snapshot.getString("rol") ?: return null
                val birthdate = snapshot.getTimestamp("birthdate") ?: return null
                User(uid, name, email, rol, birthdate)
            } catch (e: Exception) {
                null
            }
        }
    }
}
