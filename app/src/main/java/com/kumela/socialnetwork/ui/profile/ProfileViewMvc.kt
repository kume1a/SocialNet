package com.kumela.socialnetwork.ui.profile

import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface ProfileViewMvc : ObservableViewMvc<ProfileViewMvc.Listener> {
    interface Listener {
        fun onStoryItemClicked(story: Story)
        fun onPostItemClicked(post: Post)
        fun onSignOutClicked()

        fun onFollowerClicked()
        fun onFollowingClicked()

        fun onLastStoryBound()
    }

    fun bindProfileImage(imageUri: String)
    fun bindUsername(username: String)
    fun bindBio(bio: String)
    fun bindPostCount(postCount: Int)
    fun bindFollowerCount(followerCount: Int)
    fun bindFollowingCount(followingCount: Int)
    fun showNoPostsAvailable()

    fun bindPosts(posts: List<Post>)

    fun addStories(stories: List<Story>)
    fun bindStories(stories: List<Story>)
}