package com.client.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.client.myapplication.R
import com.client.myapplication.domain.model.RateDto
import com.client.myapplication.ui.theme.MyApplicationTheme
import com.client.reusablecomponents.containers.CenteredColumn
import com.client.reusablecomponents.containers.ScrollableScreen
import com.client.reusablecomponents.previews.AppScreenComponent
import com.client.reusablecomponents.previews.DevicePreviews

@Composable
internal fun HomeRoute(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val rates = mainViewModel.rates.collectAsStateWithLifecycle()
    HomeScreen(
        rates = rates.value,
    )
}

@Composable
internal fun HomeScreen(
    rates: MainViewState,
) {
    val shouldShouldContent = rememberSaveable { mutableStateOf(true) }
    if (shouldShouldContent.value) {
        ScrollableScreen {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.pln_rates),
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = stringResource(R.string._1_polish_zloty_equals_to),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyHorizontalGrid(
                    rows = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (rates is MainViewState.Success) {
                        val plnRates = rates.plnRates
                        items(plnRates.size) { index ->
                            CurrencyItem(
                                currency = plnRates[index].currency,
                                rate = plnRates[index].rate,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.euro_rates),
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = stringResource(R.string._1_euro_equals_to),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyHorizontalGrid(
                    rows = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (rates is MainViewState.Success) {
                        val euroRates = rates.euroRates
                        items(euroRates.size) { index ->
                            CurrencyItem(
                                currency = euroRates[index].currency,
                                rate = euroRates[index].rate,
                            )
                        }
                    }
                }
            }
        }
    }

    when (rates) {
        is MainViewState.Loading -> CenteredColumn {
            CircularProgressIndicator()
        }

        is MainViewState.Error -> {
            shouldShouldContent.value = false
            ErrorView(
                message = rates.message
            )
        }

        else -> Unit
    }
}

@Composable
private fun ErrorView(
    message: String
) {
    CenteredColumn {
        Text(text = stringResource(R.string.error_while_loading_data, message))
    }
}

@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    AppScreenComponent {
        MyApplicationTheme(darkTheme = true) {
            HomeScreen(
                rates = MainViewState.Success(
                    plnRates = listOf(
                        RateDto("USD", 3.5),
                        RateDto("GBP", 4.2),
                        RateDto("JPY", 0.03),
                        RateDto("EUR", 4.5)
                    ),
                    euroRates = listOf(
                        RateDto("USD", 1.2),
                        RateDto("GBP", 1.4),
                        RateDto("JPY", 0.01),
                        RateDto("EUR", 4.5)
                    )
                ),
            )
        }
    }
}
