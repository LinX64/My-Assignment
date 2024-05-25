package com.client.myapplication.repository

import app.cash.turbine.test
import com.client.myapplication.data.repository.RatesRepositoryImpl
import com.client.myapplication.repository.util.RatesStub.rates1
import com.client.myapplication.repository.util.RatesStub.rates2
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class RatesRepositoryTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())
    private lateinit var ratesRepository: RatesRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ratesRepository = mockk()
    }

    @Test
    fun `WHEN getCurrenciesRates is called THEN return flow of CurrencyRates with a loop of 1 minute`() =
        testScope.runTest {
            coEvery { ratesRepository.getCurrenciesRates() } returns flowOf(rates1, rates2)

            ratesRepository.getCurrenciesRates()
                .test {
                    awaitItem() shouldBe rates1

                    advanceTimeBy(1.minutes.inWholeMilliseconds)

                    awaitItem() shouldBe rates2

                    awaitComplete()
                }
        }

    @Test
    fun `WHEN getCurrenciesRates emits values THEN return currencies`() = testScope.runTest {
        coEvery { ratesRepository.getCurrenciesRates() } returns flowOf(rates1, rates2)

        ratesRepository.getCurrenciesRates()
            .test {
                awaitItem() shouldBe rates1

                advanceTimeBy(1.minutes.inWholeMilliseconds)

                awaitItem() shouldBe rates2

                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `WHEN getCurrenciesRates emits IOException THEN return empty list`() = testScope.runTest {
        coEvery { ratesRepository.getCurrenciesRates() } returns flow {
            throw IOException()
        }

        ratesRepository.getCurrenciesRates()
            .test {
                val error = awaitError()
                error::class shouldBe IOException::class
            }
    }

    @Test
    fun `WHEN getCurrenciesRates is not emitting values THEN retry for 20 time with 2 seconds delay`() =
        testScope.runTest {
            coEvery { ratesRepository.getCurrenciesRates() } returns flow {
                throw IOException()
            }

            ratesRepository.getCurrenciesRates()
                .test {
                    repeat(MAX_RETRIES) {
                        advanceTimeBy(RETRY_DELAY)
                    }

                    val error = awaitError()
                    error::class shouldBe IOException::class
                }
        }

    private companion object {
        private const val MAX_RETRIES = 20
        private const val RETRY_DELAY = 2000L
    }
}