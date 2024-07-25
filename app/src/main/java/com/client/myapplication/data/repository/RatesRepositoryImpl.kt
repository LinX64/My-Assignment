package com.client.myapplication.data.repository

import com.client.myapplication.data.model.Rate
import com.client.myapplication.data.model.toDomain
import com.client.myapplication.data.source.NetworkDataSource
import com.client.myapplication.domain.model.CurrencyRatesDto
import com.client.myapplication.domain.model.RateDto
import com.client.myapplication.domain.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : RatesRepository {

    // TODO: Consider using Offline-First approach to store the rates in the local database
    /**
     * This function fetches the PLN and Euro rates from the server and emits them every minute.
     * @return Flow<CurrencyRates> - a flow of CurrencyRates
     */
    @OptIn(ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
    override fun getCurrenciesRates(): Flow<CurrencyRatesDto> = ticker(
        delayMillis = RETRY_DELAY,
        initialDelayMillis = 0,
    )
        .receiveAsFlow()
        .flatMapLatest {
            fetchCurrencyRates()
        }
        .retryWhen { cause, attempt ->
            if (cause is IOException && attempt < MAX_RETRIES) {
                delay(RETRY_DELAY)
                return@retryWhen true
            } else {
                return@retryWhen false
            }
        }

    private fun fetchCurrencyRates(): Flow<CurrencyRatesDto> = combine(
        plnRatesFlow(),
        euroRatesFlow()
    ) { plnRates, euroRates ->
        CurrencyRatesDto(plnRates, euroRates)
    }

    private fun plnRatesFlow(): Flow<List<RateDto>> = flow {
        val rate = networkDataSource
            .getPlnRates()
            .conversion_rates
            .map { (currency, rate) -> Rate(currency, rate).toDomain() }
        emit(rate)
    }

    private fun euroRatesFlow(): Flow<List<RateDto>> = flow {
        val rate = networkDataSource
            .getEuroRates()
            .conversion_rates
            .map { (currency, rate) -> Rate(currency, rate).toDomain() }
        emit(rate)
    }

    private companion object {
        private const val MAX_RETRIES = 20
        private const val RETRY_DELAY = 2000L
    }
}