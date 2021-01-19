package com.kumela.socialnetwork.ui.user_profile

import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedPostResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.FollowRepository
import com.kumela.socialnetwork.network.repositories.PostRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class UserProfileViewModel(
    private val followRepository: FollowRepository,
    private val postRepository: PostRepository,
) : CachedViewModel() {
    suspend fun fetchFollowStatus(userId: Int): Result<Boolean, NetworkError> {
        return followRepository.fetchFollowStatus(userId)
    }

    suspend fun switchFollowStatus(userId: Int): Result<Unit, NetworkError> {
        return followRepository.switchFollowStatus(userId)
    }

    suspend fun fetchPosts(userId: Int): Result<PaginatedPostResponse?, NetworkError> {
        return fetchAndCachePage { page -> postRepository.getPosts(userId, page, 3) }
    }

    fun getPosts(): PaginatedPostResponse? {
        return getFromCache()
    }
}