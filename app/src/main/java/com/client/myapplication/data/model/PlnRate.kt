package com.client.myapplication.data.model

import com.client.myapplication.data.util.ConversionRatesSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlnRates(
    @SerialName("base_code")
    val baseCode: String,
    @Serializable(with = ConversionRatesSerializer::class)
    val conversion_rates: Map<String, Double>
)
// TODO: Consider changing the name of the last parameter if Kotlin Serialization allows it in the future