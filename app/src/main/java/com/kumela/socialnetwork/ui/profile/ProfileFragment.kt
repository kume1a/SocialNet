package com.kumela.socialnet.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.models.firebase.UserExtraInfoModel
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.network.firebase.UserUseCase
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnet.ui.common.controllers.BaseFragment
import com.kumela.socialnet.ui.common.viewmodels.ViewModelFactory
import com.kumela.socialnet.ui.user_list.DataType
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
    ): View? {
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

    override fun onStoryItemClicked(storyModel: StoryModel) {
        Log.d(javaClass.simpleName, "onStoryItemClicked() called with: storyModel = $storyModel")
    }

    override fun onPostItemClicked(postModel: PostModel) {
        Log.d(javaClass.simpleName, "onPostItemClicked() called with: postModel = $postModel")
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
    override fun onUserFetched(userModel: UserModel) {
        mViewMvc.bindProfileImage(userModel.imageUri)
        mViewMvc.bindUsername(userModel.username)
    }

    override fun onUserExtraInfoFetched(userExtraInfoModel: UserExtraInfoModel) {
        mViewMvc.bindBio(userExtraInfoModel.bio)
        mViewMvc.bindPostCount(userExtraInfoModel.postCount)
        mViewMvc.bindFollowerCount(userExtraInfoModel.followerCount)
        mViewMvc.bindFollowingCount(userExtraInfoModel.followingCount)
    }

    override fun onPostsFetched(posts: List<PostModel>) {
        if (posts.isNotEmpty()) {
            mViewMvc.bindPosts(posts)
        } else {
            mViewMvc.showNoPostsAvailable()
        }
    }

    override fun onStoriesFetched(storyModels: List<StoryModel>) {
        mViewMvc.addStories(storyModels)
    }
}