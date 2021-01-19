package com.kumela.socialnetwork.ui.profile

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedPostResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.PostRepository
import com.kumela.socialnetwork.network.repositories.UserRepository

/**
 * Created by Toko on 27,October,2020
 **/

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : CachedViewModel() {

    suspend fun fetchUser(): Result<User, NetworkError> {
        return getOrFetch { userRepository.fetchUser() }
    }

    suspend fun fetchPosts(userId: Int): Result<PaginatedPostResponse?, NetworkError> {
        return getOrFetchPage { page -> postRepository.getPosts(userId, page, 3) }
    }

    fun getPosts(): PaginatedPostResponse? {
        return getCachedPages()
    }
}