package com.kumela.socialnetwork.ui.explore

import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface ExploreViewMvc  : ObservableViewMvc<ExploreViewMvc.Listener> {
    interface Listener {
        fun onSearchClicked()
        fun onScrolledToBottom()
    }

    fun addPosts(posts: List<Post>)
}