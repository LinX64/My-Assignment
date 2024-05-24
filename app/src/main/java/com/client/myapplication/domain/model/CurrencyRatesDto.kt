package com.client.myapplication.domain.model

data class CurrencyRatesDto(
    val plnRates: List<RateDto>,
    val euroRates: List<RateDto>
)