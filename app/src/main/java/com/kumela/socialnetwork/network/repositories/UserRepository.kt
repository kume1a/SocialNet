package com.kumela.socialnetwork.network.repositories

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.UserMeta
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Toko on 18,January,2021
 **/

class UserRepository(private val apiService: ApiService, private val keyStore: KeyStore) {
    suspend fun fetchUser(
        userId: Int = keyStore.getUserId()
    ): Result<User, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getUser(userId) }.mapToResult()
    }

    suspend fun fetchUserMeta(
        userId: Int = keyStore.getUserId()
    ): Result<UserMeta, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getUserMeta(userId) }.mapToResult()
    }
}