package com.kumela.socialnetwork.ui.adapters.feed

import com.kumela.socialnetwork.models.firebase.FeedModel
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 01,October,2020
 **/

interface FeedItemViewMvc : ObservableViewMvc<FeedItemViewMvc.Listener> {
    interface Listener {
        fun onProfileImageClicked(position: Int, feedModel: FeedModel)
        fun onUsernameClicked(position: Int, feedModel: FeedModel)
        fun onLikeClicked(position: Int, feedModel: FeedModel)
        fun onPostDoubleClicked(position: Int, feedModel: FeedModel)
        fun onLikeCountClicked(position: Int, feedModel: FeedModel)
        fun onCommentsClicked(position: Int, feedModel: FeedModel)
        fun onCommentCountClicked(position: Int, feedModel: FeedModel)

        fun onMenuClicked()
    }

    fun bindPost(position: Int, feedModel: FeedModel)
}