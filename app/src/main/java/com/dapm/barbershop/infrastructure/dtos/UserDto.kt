package com.dapm.barbershop.infrastructure.dtos

import com.dapm.barbershop.domain.entities.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class UserDto(
    val uid: String,
    val name: String,
    val email: String,
    val rol: String,
    val birthdate: LocalDate
) {
    companion object {
        fun fromDocument(snapshot: DocumentSnapshot): UserDto? {
            return try {
                val uid = snapshot.id
                val name = snapshot.getString("name") ?: return null
                val email = snapshot.getString("email") ?: return null
                val rol = snapshot.getString("rol") ?: return null

                val birthdateTimestamp = snapshot.getTimestamp("birthdate") ?: return null
                val birthdate = birthdateTimestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

                UserDto(uid, name, email, rol, birthdate)
            } catch (e: Exception) {
                null
            }
        }

        fun fromUser(user: User): UserDto {
            val birthdate = user.birthdate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            return UserDto(user.uid, user.name, user.email, user.rol, birthdate)
        }
    }
}
