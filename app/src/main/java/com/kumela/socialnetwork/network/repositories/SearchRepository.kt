package com.kumela.socialnetwork.network.repositories

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Toko on 17,January,2021
 **/

class SearchRepository(private val apiService: ApiService) {
    suspend fun searchUsers(
        query: String
    ): Result<List<User>, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.searchUsers(query) }.mapToResult()
    }
}