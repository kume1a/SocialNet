package com.kumela.socialnetwork.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.UserExtraInfo
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import com.kumela.socialnetwork.ui.user_list.DataType
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class ProfileFragment : BaseFragment(), ProfileViewMvc.Listener,
    ProfileViewModel.Listener {

    private lateinit var mViewMvc: ProfileViewMvc
    private lateinit var mViewModel: ProfileViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mScreensNavigator: ProfileScreensNavigator
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this) // TODO: 10/17/2020 implement refresh and error handling

        mBottomNavHelper.showBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(ProfileViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(ProfileViewModel::class.java)

        val user = mViewModel.getUser()
        val userExtraInfo = mViewModel.getUserExtraInfo()
        val userPosts = mViewModel.getUserPosts()
        val userStories = mViewModel.getUserStories()

        if (user != null) {
            onUserFetched(user)
        } else {
            mViewModel.fetchUserAndNotify()
        }

        if (userExtraInfo != null) {
            onUserExtraInfoFetched(userExtraInfo)
        } else {
            mViewModel.fetchUserExtraInfoAndNotify()
        }

        if (userPosts != null) {
            onPostsFetched(userPosts)
        } else {
            mViewModel.fetchUserPostsAndNotify()
        }

        if (userStories.isNotEmpty()) {
            onStoriesFetched(userStories)
        } else {
            mViewModel.fetchNextStoriesPageAndNotify()
        }
    }

    override fun onStart() {
        super.onStart()
        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        mViewMvc.unregisterListener()
        mViewModel.registerListener(this)
    }

    override fun onStoryItemClicked(story: Story) {
        Log.d(javaClass.simpleName, "onStoryItemClicked() called with: storyModel = $story")
    }

    override fun onPostItemClicked(post: Post) {
        Log.d(javaClass.simpleName, "onPostItemClicked() called with: postModel = $post")
    }

    override fun onSignOutClicked() {
        activity?.viewModelStore?.clear()
        UserUseCase.signOut()
        mScreensNavigator.toSignIn()
    }

    override fun onFollowerClicked() {
        mScreensNavigator.toUsersList(DataType.FOLLOWERS, UserUseCase.uid)
    }

    override fun onFollowingClicked() {
        mScreensNavigator.toUsersList(DataType.FOLLOWING, UserUseCase.uid)
    }

    override fun onLastStoryBound() {
        mViewModel.fetchNextStoriesPageAndNotify()
    }

    // view model callbacks
    override fun onUserFetched(user: User) {
        mViewMvc.bindProfileImage(user.imageUrl)
        mViewMvc.bindUsername(user.name)
    }

    override fun onUserExtraInfoFetched(userExtraInfo: UserExtraInfo) {
        mViewMvc.bindBio(userExtraInfo.bio)
        mViewMvc.bindPostCount(userExtraInfo.postCount)
        mViewMvc.bindFollowerCount(userExtraInfo.followerCount)
        mViewMvc.bindFollowingCount(userExtraInfo.followingCount)
    }

    override fun onPostsFetched(posts: List<Post>) {
        if (posts.isNotEmpty()) {
            mViewMvc.bindPosts(posts)
        } else {
            mViewMvc.showNoPostsAvailable()
        }
    }

    override fun onStoriesFetched(stories: List<Story>) {
        mViewMvc.addStories(stories)
    }
}