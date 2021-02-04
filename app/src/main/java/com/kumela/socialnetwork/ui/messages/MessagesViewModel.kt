package com.kumela.socialnetwork.ui.messages

import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedUserResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.UserRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class MessagesViewModel(
    private val userRepository: UserRepository,
) : CachedViewModel() {

    fun getCachedFollowingUsers(): PaginatedUserResponse? = getFromCache()
    suspend fun fetchFollowingUsers(): Result<PaginatedUserResponse?, NetworkError> {
        return fetchAndCachePage { page ->
            userRepository.fetchFollowingUsers(page, 10)
        }
    }
}