package com.dapm.barbershop.domain.entities

data class Barber(
    var uid: String = "",
    val name: String = "",
    val lastName: String = "",
    val workingDays: List<String> = emptyList(),
    val startTime: String = "",
    val endTime: String = "",
    var state: String = ""
){

}
