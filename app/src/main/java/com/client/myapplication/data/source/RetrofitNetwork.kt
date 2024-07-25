package com.client.myapplication.data.source

import com.client.myapplication.data.model.EuroRates
import com.client.myapplication.data.model.PlnRates
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "https://v6.exchangerate-api.com/v6/118e47ca42f9a041c9a9d35f/"

private interface CurrenciesApi {

    @GET("latest/PLN")
    suspend fun getPlnRates(): PlnRates

    @GET("latest/EUR")
    suspend fun getEuroRates(): EuroRates
}

class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : NetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(CurrenciesApi::class.java)

    override suspend fun getPlnRates(): PlnRates = networkApi.getPlnRates()

    override suspend fun getEuroRates(): EuroRates = networkApi.getEuroRates()
}
