package com.kumela.socialnetwork.ui.adapters.feed

import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 01,October,2020
 **/

interface FeedItemViewMvc : ObservableViewMvc<FeedItemViewMvc.Listener> {
    interface Listener {
        fun onProfileImageClicked(position: Int, feed: Feed)
        fun onUsernameClicked(position: Int, feed: Feed)
        fun onLikeClicked(position: Int, feed: Feed)
        fun onPostDoubleClicked(position: Int, feed: Feed)
        fun onLikeCountClicked(position: Int, feed: Feed)
        fun onCommentsClicked(position: Int, feed: Feed)
        fun onCommentCountClicked(position: Int, feed: Feed)

        fun onMenuClicked()
    }

    fun bindPost(position: Int, feed: Feed)
}