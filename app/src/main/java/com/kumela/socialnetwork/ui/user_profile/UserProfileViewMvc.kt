package com.kumela.socialnet.ui.user_profile

import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 17,October,2020
 **/

interface UserProfileViewMvc : ObservableViewMvc<UserProfileViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()
        fun onFollowClicked()
        fun onSendMessageClicked()
        fun onStoryItemClicked(storyModel: StoryModel)
        fun onPostItemClicked(postModel: PostModel)

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
    fun setFollowingButtonText(text: String)

    fun bindPosts(posts: List<PostModel>)

    fun bindStories(stories: List<StoryModel>)
    fun addStories(storyModels: List<StoryModel>)
}