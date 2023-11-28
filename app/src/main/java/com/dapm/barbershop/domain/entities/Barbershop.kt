package com.dapm.barbershop.domain.entities

data class Barbershop(
    var uid: String = "",
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val image: String = "",
    var state: String = "",
    val services: List<ServiceBarbershop> = emptyList()) {
}