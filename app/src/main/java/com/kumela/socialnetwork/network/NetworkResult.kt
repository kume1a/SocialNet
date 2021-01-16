package com.kumela.socialnetwork.network

/**
 * Created by Toko on 15,January,2021
 **/

sealed class NetworkResult<out T> {
    data class Success<T>(val value: T) : NetworkResult<T>()
    data class HttpError(val statusCode: Int, val error: Throwable) : NetworkResult<Nothing>()
    data class Error(val error: Throwable) : NetworkResult<Nothing>()
}

sealed class NetworkError {
    data class HttpError(val statusCode: Int) : NetworkError()
    object Error : NetworkError()
}
