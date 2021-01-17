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
        fun onLikeCountClicked(postId: String)
        fun onCommentClicked(postId: String)
        fun onLikeClicked(position: Int, feedModel: Feed)
        fun onPostDoubleClick(position: Int, feedModel: Feed)
    }

    fun bindStories(users: List<User>)
    fun addStory(user: User)

    fun addPost(post: Feed)
    fun bindPosts(posts: List<Feed>)
    fun updatePost(position: Int, feedModel: Feed)

    fun getCurrentFeedListOffset(): Int
    fun scrollFeedListTo(feedListOffset: Int)
}