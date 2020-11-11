package com.kumela.socialnet.ui.home

import com.kumela.socialnet.models.firebase.FeedModel
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface HomeViewMvc : ObservableViewMvc<HomeViewMvc.Listener> {
    interface Listener {
        fun onStoryClicked(position: Int, user: UserModel)
        fun onScrolledToBottom()

        fun onUserProfileOrUsernameClicked(user: UserModel)
        fun onLikeCountClicked(postId: String)
        fun onCommentClicked(postId: String)
        fun onLikeClicked(position: Int, feedModel: FeedModel)
        fun onPostDoubleClick(position: Int, feedModel: FeedModel)
    }

    fun bindStories(users: List<UserModel>)
    fun addStory(user: UserModel)

    fun addPost(post: FeedModel)
    fun bindPosts(posts: List<FeedModel>)
    fun updatePost(position: Int, feedModel: FeedModel)

    fun getCurrentFeedListOffset(): Int
    fun scrollFeedListTo(feedListOffset: Int)
}