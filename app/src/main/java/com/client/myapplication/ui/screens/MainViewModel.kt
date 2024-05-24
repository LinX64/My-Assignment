package com.client.myapplication.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.myapplication.data.util.NetworkResult
import com.client.myapplication.data.util.asResult
import com.client.myapplication.domain.model.RateDto
import com.client.myapplication.domain.repository.RatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
   ratesRepository: RatesRepository
) : ViewModel() {

    val rates = ratesRepository.getCurrenciesRates()
        .asResult()
        .map { currencyRates ->
            when (currencyRates) {
                is NetworkResult.Success -> {
                    val plnRates = currencyRates.data.plnRates.filter { it.currency != "PLN" }
                    val euroRates = currencyRates.data.euroRates.filter { it.currency != "EUR" }
                    MainViewState.Success(plnRates, euroRates)
                }

                is NetworkResult.Error -> MainViewState.Error(
                    currencyRates.exception.message ?: "An error occurred"
                )

                else -> MainViewState.Loading
            }

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainViewState.Loading
        )
}

sealed class MainViewState {
    data object Loading : MainViewState()

    data class Success(
        val plnRates: List<RateDto>,
        val euroRates: List<RateDto>
    ) : MainViewState()

    data class Error(val message: String) : MainViewState()
}