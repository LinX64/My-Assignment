package com.client.myapplication.repository.util

import com.client.myapplication.data.model.PlnRates
import com.client.myapplication.domain.model.CurrencyRatesDto
import com.client.myapplication.domain.model.RateDto
import com.google.gson.GsonBuilder

object RatesStub {

    val rates1 = CurrencyRatesDto(
        plnRates = getPlnRates(),
        euroRates = getEuroRates()
    )
    val rates2 = CurrencyRatesDto(
        plnRates = getPlnRates(),
        euroRates = getEuroRates()
    )
    private const val PLN_RATES = """
        {
            "conversion_rates": {
                "USD": 0.25,
                "EUR": 0.22,
                "GBP": 0.19
            }
        }
    """

    private const val EUR_RATES = """
        {
            "conversion_rates": {
                "USD": 1.15,
                "PLN": 4.55,
                "GBP": 0.87
            }
        }
    """

    private fun getPlnRates(): List<RateDto> {
        val gson = GsonBuilder().create()
        val conversionRatesDto = gson.fromJson(PLN_RATES, PlnRates::class.java)
        return conversionRatesDto.conversion_rates.map { RateDto(it.key, it.value) }
    }

    private fun getEuroRates(): List<RateDto> {
        val gson = GsonBuilder().create()
        val conversionRatesDto = gson.fromJson(EUR_RATES, PlnRates::class.java)
        return conversionRatesDto.conversion_rates.map { RateDto(it.key, it.value) }
    }
}