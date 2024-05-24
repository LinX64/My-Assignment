package com.client.myapplication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EuroRates(
    @SerialName("base_code")
    val baseCode: String,
    @SerialName("conversion_rates")
    val conversion_rates: Map<String, Double>
)