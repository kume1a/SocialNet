package com.kumela.socialnet.ui.adapters.posts

import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 05,October,2020
 **/

interface PostItemViewMvc : ObservableViewMvc<PostItemViewMvc.Listener> {
    interface Listener {
        fun onPostClicked(postModel: PostModel)
    }

    fun bindPostModel(postModel: PostModel)
}