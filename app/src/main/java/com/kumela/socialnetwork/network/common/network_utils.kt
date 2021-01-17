package com.kumela.socialnetwork.network.common

import android.util.Log
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.NetworkResult
import com.kumela.socialnetwork.network.firebase.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by Toko on 15,January,2021
 **/

suspend fun <T> safeCall(
    apiCall: suspend () -> Response<T>
): NetworkResult<T> = withContext(Dispatchers.IO) {
    try {
        val response = apiCall.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            return@withContext NetworkResult.Success(body)
        } else {
            val throwable = HttpException(response)
            Log.e(javaClass.simpleName, "safeCall: ", throwable)
            return@withContext NetworkResult.HttpError(response.code(), throwable)
        }
    } catch (throwable: Throwable) {
        Log.e(javaClass.simpleName, "safeCall: ", throwable)
        return@withContext when (throwable) {
            is HttpException -> NetworkResult.HttpError(throwable.code(), throwable)
            else -> NetworkResult.Error(throwable)
        }
    }
}

fun <T, R> NetworkResult<T>.mapToResult(
    onSuccess: (body: T) -> Result<R, NetworkError>
): Result<R, NetworkError> {
    return when (this) {
        is NetworkResult.Success -> onSuccess.invoke(this.body)
        is NetworkResult.HttpError -> Result.Failure(NetworkError.HttpError(this.statusCode))
        is NetworkResult.Error -> Result.Failure(NetworkError.Error)
    }
}

fun <T> NetworkResult<T>.mapToResult(): Result<T, NetworkError> {
    return when (this) {
        is NetworkResult.Success -> Result.Success(this.body)
        is NetworkResult.HttpError -> Result.Failure(NetworkError.HttpError(this.statusCode))
        is NetworkResult.Error -> Result.Failure(NetworkError.Error)
    }
}
