package com.kumela.socialnetwork.network.authentication

import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.api.SigninBody
import com.kumela.socialnetwork.network.api.SignupBody
import com.kumela.socialnetwork.network.firebase.Result
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
    ): Result<String, Exception> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.signin(SigninBody(email, password))
            val body = response.body()
            if (response.isSuccessful && body != null) {
                keyStore.saveKey(body.token)
                return@withContext Result.Success(body.userId)
            }
            return@withContext Result.Failure(Exception("unknown error status code: ${response.code()}"))
        } catch (e: Exception) {
            return@withContext Result.Failure(e)
        }
    }

    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Result<String, Exception> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.signup(SignupBody(name, email, password))
            val body = response.body()
            if (response.isSuccessful && body != null) {
                keyStore.saveKey(body.token)
                return@withContext Result.Success(body.userId)
            }
            return@withContext Result.Failure(Exception("unknown error status code: ${response.code()}"))
        } catch (e: Exception) {
            return@withContext Result.Failure(e)
        }
    }

}