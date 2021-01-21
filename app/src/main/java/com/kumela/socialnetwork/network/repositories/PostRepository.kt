package com.kumela.socialnetwork.network.repositories

import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.api.PaginatedFeedResponse
import com.kumela.socialnetwork.network.api.PaginatedPostResponse
import com.kumela.socialnetwork.network.api.PostBody
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
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

    suspend fun getPosts(
        userId: Int,
        page: Int,
        limit: Int,
    ): Result<PaginatedPostResponse, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getPosts(userId, page, limit) }.mapToResult()
    }

    suspend fun fetchFeedPosts(
        userId: Int,
        page: Int,
        limit: Int
    ): Result<PaginatedFeedResponse, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getFeedPosts(userId, page, limit) }.mapToResult()
    }
}