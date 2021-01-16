package com.kumela.socialnetwork.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Created by Toko on 15,January,2021
 **/

suspend fun <T> safeCall(
    apiCall: suspend () -> T
): NetworkResult<T> = withContext(Dispatchers.IO) {
    try {
        return@withContext NetworkResult.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        return@withContext when (throwable) {
            is HttpException -> NetworkResult.HttpError(throwable.code(), throwable)
            else -> NetworkResult.Error(throwable)
        }
    }
}
