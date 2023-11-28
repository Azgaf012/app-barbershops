package com.dapm.barbershop.infrastructure.dtos

import com.dapm.barbershop.domain.entities.ServiceBarbershop

data class ServiceBarbershopDto(
    val uid: String? = null,
    val name: String? = null,
    val price: String? = null,
    val description: String? = null
) {
    companion object {
        fun fromServiceBarbershop(serviceBarbershop: ServiceBarbershop): ServiceBarbershopDto {
            return ServiceBarbershopDto(
                uid = serviceBarbershop.uid,
                name = serviceBarbershop.name,
                price = serviceBarbershop.price,
                description = serviceBarbershop.description
            )
        }
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "uid" to uid.orEmpty(),
            "name" to name.orEmpty(),
            "price" to price.orEmpty(),
            "description" to description.orEmpty()
        )
    }

    fun toServiceBarbershop(): ServiceBarbershop {
        return ServiceBarbershop(
            uid = uid ?: "",
            name = name ?: "",
            price = price ?: "",
            description = description ?: ""
        )
    }
}