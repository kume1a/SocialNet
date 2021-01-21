package com.kumela.socialnetwork.ui.explore

import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedPostResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.PostRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

/**
 * Created by Toko on 05,November,2020
 **/

class ExploreViewModel(
    private val postRepository: PostRepository,
) : CachedViewModel() {

    suspend fun fetchPosts(): Result<PaginatedPostResponse?, NetworkError> {
        return fetchAndCachePage { page ->
            postRepository.fetchExplorePosts(page, 3)
        }
    }

    fun getCachedPosts(): PaginatedPostResponse? = getFromCache()
}