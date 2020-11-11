package com.kumela.socialnet.ui.explore

import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface ExploreViewMvc  : ObservableViewMvc<ExploreViewMvc.Listener> {
    interface Listener {
        fun onSearchClicked()
        fun onScrolledToBottom()
    }

    fun bindPosts(postModels: List<PostModel>)
    fun addPosts(postModels: List<PostModel>)
}