package com.kumela.socialnetwork.ui.adapters.posts

import com.kumela.socialnetwork.models.list.PostModel
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 05,October,2020
 **/

interface PostItemViewMvc : ObservableViewMvc<PostItemViewMvc.Listener> {
    interface Listener {
        fun onPostClicked(postModel: PostModel)
    }

    fun bindPostModel(postModel: PostModel)
}