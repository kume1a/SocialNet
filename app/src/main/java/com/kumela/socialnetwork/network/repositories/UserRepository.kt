package com.kumela.socialnetwork.network.repositories

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
import com.kumela.socialnetwork.network.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Toko on 18,January,2021
 **/

class UserRepository(private val apiService: ApiService, private val keyStore: KeyStore) {
    suspend fun fetchUser(): Result<User, NetworkError> = withContext(Dispatchers.IO) {
        val userId = keyStore.getUserId()
        return@withContext safeCall { apiService.getUser(userId) }.mapToResult()
    }
}