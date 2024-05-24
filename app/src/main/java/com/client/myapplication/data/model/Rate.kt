package com.client.myapplication.data.model

import com.client.myapplication.domain.model.RateDto
import kotlinx.serialization.Serializable

@Serializable
data class Rate(
    val currency: String,
    val rate: Double
)

fun Rate.toDomain() = RateDto(
    currency = currency,
    rate = rate
)