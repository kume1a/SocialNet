package com.kumela.socialnetwork.network.repositories

import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
import com.kumela.socialnetwork.network.firebase.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Toko on 17,January,2021
 **/

class FollowRepository(private val apiService: ApiService) {
    suspend fun fetchFollowStatus(
        userId: Int
    ): Result<Boolean, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getFollowStatus(userId) }.mapToResult()
    }

    suspend fun switchFollowStatus(
        userId: Int
    ): Result<Unit, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.switchFollowStatus(userId) }.mapToResult()
    }
}