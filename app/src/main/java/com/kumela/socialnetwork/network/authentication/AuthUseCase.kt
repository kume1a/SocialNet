package com.kumela.socialnetwork.network.authentication

import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.api.SigninBody
import com.kumela.socialnetwork.network.api.SignupBody
import com.kumela.socialnetwork.network.firebase.Result
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
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
    ): Result<Int, NetworkError> = withContext(Dispatchers.IO) {
        val result = safeCall { apiService.signin(SigninBody(email, password)) }
        return@withContext result.mapToResult { body ->
            keyStore.saveKey(body.token)
            Result.Success(body.userId)
        }
    }

    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Result<Int, NetworkError> = withContext(Dispatchers.IO) {
        val result = safeCall { apiService.signup(SignupBody(name, email, password)) }
        return@withContext result.mapToResult { body ->
            keyStore.saveKey(body.token)
            Result.Success(body.userId)
        }
    }

}