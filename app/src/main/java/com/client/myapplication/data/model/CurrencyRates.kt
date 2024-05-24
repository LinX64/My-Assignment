package com.client.myapplication.data.model

import com.client.myapplication.domain.model.CurrencyRatesDto
import com.client.myapplication.domain.model.RateDto

data class CurrencyRates(
    val plnRates: List<RateDto>,
    val euroRates: List<RateDto>
)

fun CurrencyRates.toDomain() = CurrencyRatesDto(
    plnRates = plnRates,
    euroRates = euroRates
)