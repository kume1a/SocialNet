package com.kumela.socialnetwork.ui.home

import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface HomeViewMvc : ObservableViewMvc<HomeViewMvc.Listener> {
    interface Listener {
        fun onStoryClicked(position: Int, user: User)
        fun onScrolledToBottom()

        fun onUserProfileOrUsernameClicked(user: User)
        fun onLikeCountClicked(postId: Int)
        fun onCommentClicked(postId: Int)
        fun onLikeClicked(position: Int, feed: Feed)
        fun onPostDoubleClick(position: Int, feed: Feed)
    }

    fun addStories(users: List<User>)

    fun addPosts(posts: List<Feed>)
    fun updatePost(position: Int, feed: Feed)
}