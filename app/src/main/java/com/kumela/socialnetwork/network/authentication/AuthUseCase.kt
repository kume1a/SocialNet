package com.kumela.socialnetwork.network.authentication

import android.util.Log
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.NetworkResult
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.api.SigninBody
import com.kumela.socialnetwork.network.api.SignupBody
import com.kumela.socialnetwork.network.firebase.Result
import com.kumela.socialnetwork.network.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Created by Toko on 26,September,2020
 **/

class AuthUseCase(
    private val apiService: ApiService,
    private val keyStore: KeyStore
) {

    suspend fun signin(
        email: String,
        password: String
    ): Result<String, NetworkError> = withContext(Dispatchers.IO) {
        val result = safeCall { apiService.signin(SigninBody(email, password)) }
        return@withContext when (result) {
            is NetworkResult.Success -> {
                val body = result.value.body()
                if (result.value.isSuccessful && body != null) {
                    keyStore.saveKey(body.token)
                    Result.Success(body.userId)
                } else {
                    Result.Failure(NetworkError.HttpError(result.value.code()))
                }
            }
            is NetworkResult.HttpError -> Result.Failure(NetworkError.HttpError(result.statusCode))
            is NetworkResult.Error -> Result.Failure(NetworkError.Error)
        }
    }

    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Result<String, NetworkError> = withContext(Dispatchers.IO) {
        Log.d(javaClass.simpleName, "signup: called")
        val result = safeCall { apiService.signup(SignupBody(name, email, password)) }
        Log.d(javaClass.simpleName, "signup: result = $result")
        return@withContext when (result) {
            is NetworkResult.Success -> {
                val body = result.value.body()
                if (result.value.isSuccessful && body != null) {
                    keyStore.saveKey(body.token)
                    return@withContext Result.Success(body.userId)
                } else {
                    Result.Failure(NetworkError.HttpError(result.value.code()))
                }
            }
            is NetworkResult.HttpError -> Result.Failure(NetworkError.HttpError(result.statusCode))
            is NetworkResult.Error -> Result.Failure(NetworkError.Error)
        }
    }

}