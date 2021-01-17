package com.kumela.socialnetwork.ui.adapters.feed

import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 01,October,2020
 **/

interface FeedItemViewMvc : ObservableViewMvc<FeedItemViewMvc.Listener> {
    interface Listener {
        fun onProfileImageClicked(position: Int, feedModel: Feed)
        fun onUsernameClicked(position: Int, feedModel: Feed)
        fun onLikeClicked(position: Int, feedModel: Feed)
        fun onPostDoubleClicked(position: Int, feedModel: Feed)
        fun onLikeCountClicked(position: Int, feedModel: Feed)
        fun onCommentsClicked(position: Int, feedModel: Feed)
        fun onCommentCountClicked(position: Int, feedModel: Feed)

        fun onMenuClicked()
    }

    fun bindPost(position: Int, feedModel: Feed)
}