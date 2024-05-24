package com.client.myapplication.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val exception: Throwable) : NetworkResult<Nothing>
    data object Loading : NetworkResult<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<NetworkResult<T>> = map<T, NetworkResult<T>> { NetworkResult.Success(it) }
    .onStart { emit(NetworkResult.Loading) }
    .catch { emit(NetworkResult.Error(it)) }
