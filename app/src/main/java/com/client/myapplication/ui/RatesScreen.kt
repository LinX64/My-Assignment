package com.client.myapplication.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.client.myapplication.ui.theme.MyApplicationTheme

@Composable
fun RatesScreen(euroRates: List<String>, plnRates: List<String>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Section(title = "Euro Rates", rates = euroRates)
            Spacer(modifier = Modifier.height(24.dp))
            Section(title = "PLN Rates", rates = plnRates)
        }
    }
}

@Composable
fun Section(title: String, rates: List<String>) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        fontSize = 24.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(rates.size) { rate ->
            RateItem(rate = rates[rate])
        }
    }
}

@Composable
fun RateItem(rate: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = rate,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RatesScreenPreview() {
    MyApplicationTheme {
        RatesScreen(
            euroRates = listOf("1.23", "1.24", "1.25"),
            plnRates = listOf("4.56", "4.57", "4.58")
        )
    }
}