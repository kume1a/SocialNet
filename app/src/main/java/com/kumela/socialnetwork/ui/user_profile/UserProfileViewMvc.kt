package com.kumela.socialnetwork.ui.user_profile

import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 17,October,2020
 **/

interface UserProfileViewMvc : ObservableViewMvc<UserProfileViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()
        fun onFollowClicked()
        fun onSendMessageClicked()
        fun onStoryItemClicked(story: Story)
        fun onPostItemClicked(post: Post)

        fun onFollowerClicked()
        fun onFollowingClicked()

        fun onLastPostBound()
        fun onLastStoryBound()
    }

    fun bindProfileImage(imageUri: String)
    fun bindUsername(username: String)
    fun bindBio(bio: String)
    fun bindPostCount(postCount: Int)
    fun bindFollowerCount(followerCount: Int)
    fun bindFollowingCount(followingCount: Int)
    fun showNoPostsAvailable()
    fun setFollowingButtonText(text: String)
    fun getFollowingButtonText(): String

    fun addPosts(posts: List<Post>)

    fun bindStories(stories: List<Story>)
    fun addStories(stories: List<Story>)
}