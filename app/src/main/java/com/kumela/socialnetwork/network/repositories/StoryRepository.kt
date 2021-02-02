package com.kumela.socialnetwork.network.repositories

import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.api.PaginatedUserResponse
import com.kumela.socialnetwork.network.api.StoryBody
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryRepository(private val apiService: ApiService) {
    suspend fun createStory(
        imageUrl: String
    ): Result<Story, NetworkError> = withContext(Dispatchers.IO) {
        val body = StoryBody(imageUrl)
        return@withContext safeCall { apiService.createStory(body) }.mapToResult()
    }

    suspend fun fetchUserStories(
        userId: Int
    ): Result<List<Story>, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getUserStories(userId) }.mapToResult()
    }

    suspend fun fetchFeedStories(
        page: Int,
        limit: Int,
    ): Result<PaginatedUserResponse, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getFeedStories(page, limit) }.mapToResult()
    }
}