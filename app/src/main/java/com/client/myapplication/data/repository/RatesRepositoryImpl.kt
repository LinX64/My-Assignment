package com.client.myapplication.data.repository

import com.client.myapplication.data.model.CurrencyRates
import com.client.myapplication.data.model.Rate
import com.client.myapplication.data.model.toDomain
import com.client.myapplication.data.source.NetworkDataSource
import com.client.myapplication.domain.model.CurrencyRatesDto
import com.client.myapplication.domain.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class RatesRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : RatesRepository {

    // TODO: Consider using Offline-First approach to store the rates in the local database
    /**
     * This function fetches the PLN and Euro rates from the server and emits them every minute.
     * @return Flow<CurrencyRates> - a flow of CurrencyRates
     */
    override fun getCurrenciesRates(): Flow<CurrencyRatesDto> = flow {
        while (true) {
            val rates = fetchCurrencyRates()
            emit(rates)

            delay(1.minutes)
        }
    }
        .flowOn(Dispatchers.IO)
        .retryWhen { cause, attempt ->
            if (cause is IOException && attempt < MAX_RETRIES) {
                delay(RETRY_DELAY)
                return@retryWhen true
            } else {
                return@retryWhen false
            }
        }

    private suspend fun fetchCurrencyRates(): CurrencyRatesDto = coroutineScope {
        val plnRatesDeferred = async { mapPlnRateToList() }
        val euroRatesDeferred = async { mapEuroRateToList() }

        val plnRates = plnRatesDeferred.await()
        val euroRates = euroRatesDeferred.await()

        CurrencyRates(plnRates, euroRates).toDomain()
    }

    private suspend fun mapPlnRateToList() = networkDataSource
        .getPlnRates()
        .conversion_rates
        .map { (currency, rate) -> Rate(currency, rate).toDomain() }

    private suspend fun mapEuroRateToList() = networkDataSource
        .getEuroRates()
        .conversion_rates
        .map { (currency, rate) -> Rate(currency, rate).toDomain() }

    private companion object {
        private const val MAX_RETRIES = 20
        private const val RETRY_DELAY = 2000L
    }
}