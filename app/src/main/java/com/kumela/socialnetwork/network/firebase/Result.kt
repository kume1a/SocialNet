package com.kumela.socialnet.network.firebase

/**
 * Created by Toko on 05,November,2020
 **/

sealed class Result<out S, out F: Throwable> {
    data class Success<out S, out F: Throwable>(val value: S) : Result<S, F>()
    data class Failure<out S, out F: Throwable>(val failure: F) : Result<S, F>()
}

fun <S, F : Throwable> Result<S, F>.fold(onSuccess: (S) -> Unit, onFailure: (F) -> Unit) {
    when (this) {
        is Result.Success -> onSuccess(value)
        is Result.Failure -> onFailure(failure)
    }
}