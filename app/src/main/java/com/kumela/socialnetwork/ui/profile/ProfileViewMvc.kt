package com.kumela.socialnet.ui.profile

import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface ProfileViewMvc : ObservableViewMvc<ProfileViewMvc.Listener> {
    interface Listener {
        fun onStoryItemClicked(storyModel: StoryModel)
        fun onPostItemClicked(postModel: PostModel)
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

    fun bindPosts(posts: List<PostModel>)

    fun addStories(storyModels: List<StoryModel>)
    fun bindStories(stories: List<StoryModel>)
}