package com.kumela.socialnetwork.ui.story_presenter

import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedUserResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.StoryRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

/**
 * Created by Toko on 09,November,2020
 **/

class StoryPresenterViewModel(
    private val storyRepository: StoryRepository
) : CachedViewModel() {
    private val userIdToStoryCache = HashMap<Int, List<Story>>()

    fun getCachedStoryAuthors(): PaginatedUserResponse? = getFromCache()
    suspend fun fetchFeedStories(): Result<PaginatedUserResponse?, NetworkError> {
        return fetchAndCachePage { page ->
            storyRepository.fetchFeedStories(page, 10)
        }
    }

    suspend fun fetchStories(userId: Int): Result<List<Story>, NetworkError> {
        val stories = storyRepository.fetchUserStories(userId)
        if (stories is Result.Success) {
            userIdToStoryCache.put(userId, stories.value)
        }
        return stories
    }

    fun getStories(currentUserId: Int): List<Story>? {
        return userIdToStoryCache[currentUserId]
    }
}