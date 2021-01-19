package com.kumela.socialnetwork.ui.search

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.SearchRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

/**
 * Created by Toko on 19,January,2021
 **/

class SearchViewModel(
    private val searchRepository: SearchRepository
): CachedViewModel() {
    suspend fun searchUsers(query: String): Result<List<User>, NetworkError> {
        return fetchAndCache { searchRepository.searchUsers(query) }
    }

    fun getCachedUsers(): List<User>? {
        return getFromCache()
    }
}