package com.client.myapplication.domain.repository

import com.client.myapplication.domain.model.CurrencyRatesDto
import kotlinx.coroutines.flow.Flow

interface RatesRepository {
     fun getCurrenciesRates(): Flow<CurrencyRatesDto>
}