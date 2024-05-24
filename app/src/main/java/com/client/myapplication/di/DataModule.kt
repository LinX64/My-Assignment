package com.client.myapplication.di

import com.client.myapplication.data.repository.RatesRepositoryImpl
import com.client.myapplication.data.source.NetworkDataSource
import com.client.myapplication.data.source.RetrofitNetwork
import com.client.myapplication.domain.repository.RatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindRatesRepository(
        ratesRepositoryImpl: RatesRepositoryImpl
    ): RatesRepository

    @Binds
    fun bindNetworkDataSource(
        retrofitNetwork: RetrofitNetwork
    ): NetworkDataSource
}
