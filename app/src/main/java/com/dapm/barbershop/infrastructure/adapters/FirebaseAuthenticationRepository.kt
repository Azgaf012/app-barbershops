package com.dapm.barbershop.infrastructure.adapters

import com.dapm.barbershop.domain.entities.User
import com.dapm.barbershop.domain.ports.AuthenticationRepository
import com.dapm.barbershop.infrastructure.dtos.UserDto
import com.dapm.barbershop.infrastructure.exceptions.AuthenticationException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject


class FirebaseAuthenticationRepository @Inject constructor(): AuthenticationRepository  {


    private val firebaseAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    override suspend fun login(email: String, password: String): UserDto  {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw AuthenticationException("Failed to login")
            val doc = db.collection("users").document(user.uid).get().await()
            return UserDto.fromDocument(doc) ?: throw AuthenticationException("Failed to retrieve user data")
        } catch (e: Exception) {
            throw AuthenticationException(e.message ?: "Unknown error")
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        rol: String,
        birthdate: LocalDate
    ): UserDto {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userAuth = result.user ?: throw AuthenticationException("Failed to register")

            val date = Date.from(birthdate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val birthdateTimestamp = Timestamp(date)

            val user = User(userAuth.uid, name, email, rol, birthdateTimestamp)
            db.collection("users").document(userAuth.uid).set(user).await()

            return UserDto.fromUser(user)

        } catch (e: Exception) {
            throw AuthenticationException(e.message ?: "Unknown error")
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}