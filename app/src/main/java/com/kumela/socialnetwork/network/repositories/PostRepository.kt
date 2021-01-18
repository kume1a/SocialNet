package com.kumela.socialnetwork.network.repositories

import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.api.PostBody
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
import com.kumela.socialnetwork.network.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Toko on 18,January,2021
 **/

class PostRepository(private val apiService: ApiService) {
    suspend fun createPost(
        imageUrl: String, header: String, description: String
    ): Result<Unit, NetworkError> = withContext(Dispatchers.IO) {
        val body = PostBody(imageUrl, header, description)
        return@withContext safeCall { apiService.createPost(body) }.mapToResult()
    }
}