package com.kumela.socialnetwork.network.repositories

import android.os.CountDownTimer
import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.UserMeta
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.api.PaginatedUserResponse
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    fun registerUserPresenceListener() {
        object : CountDownTimer(Long.MAX_VALUE, Constants.INTERVAL_ONLINE_UPDATE) {
            override fun onTick(millisUntilFinished: Long) {
                GlobalScope.launch {
                    apiService.postUserPresence()
                }
            }

            override fun onFinish() {}
        }.start()
    }

    suspend fun fetchFollowingUsers(
        page: Int,
        limit: Int
    ) : Result<PaginatedUserResponse, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getFollowingUsers(page, limit) }.mapToResult()
    }
}