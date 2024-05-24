package com.client.myapplication.data.source

import com.client.myapplication.data.model.EuroRates
import com.client.myapplication.data.model.PlnRates

interface NetworkDataSource {
    suspend fun getPlnRates(): PlnRates
    suspend fun getEuroRates(): EuroRates
}
