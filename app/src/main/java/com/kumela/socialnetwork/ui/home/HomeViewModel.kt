package com.kumela.socialnetwork.ui.home

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedFeedResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.PostRepository
import com.kumela.socialnetwork.network.repositories.UserRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

/**
 * Created by Toko on 23,October,2020
 **/

class HomeViewModel(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) : CachedViewModel() {
    suspend fun fetchFeedPosts(): Result<PaginatedFeedResponse?, NetworkError> {
        return fetchAndCachePage { page ->
            postRepository.fetchFeedPosts(page, 5)
        }
    }

    suspend fun likePost(postId: Int) = postRepository.likePost(postId)
    suspend fun dislikePost(postId: Int) = postRepository.dislikePost(postId)

    suspend fun fetchUser(): Result<User, NetworkError> {
        return fetchAndCache { userRepository.fetchUser() }
    }

    fun getCachedFeedPosts(): PaginatedFeedResponse? = getFromCache()
}