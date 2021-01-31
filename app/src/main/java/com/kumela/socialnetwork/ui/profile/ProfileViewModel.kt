package com.kumela.socialnetwork.ui.profile

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.UserMeta
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedPostResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.PostRepository
import com.kumela.socialnetwork.network.repositories.UserRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : CachedViewModel() {

    suspend fun fetchUser(): Result<User, NetworkError> {
        return fetchAndCache { userRepository.fetchUser() }
    }

    suspend fun fetchPosts(userId: Int): Result<PaginatedPostResponse?, NetworkError> {
        return fetchAndCachePage { page -> postRepository.getPosts(userId, page, 3) }
    }

    fun getPosts(): PaginatedPostResponse? {
        return getFromCache()
    }

    suspend fun fetchUserMeta(): Result<UserMeta, NetworkError> {
        return fetchAndCache { userRepository.fetchUserMeta() }
    }
}