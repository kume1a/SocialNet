package com.kumela.socialnetwork.ui.user_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.UserExtraInfo
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import com.kumela.socialnetwork.ui.user_list.DataType
import javax.inject.Inject

/**
 * Created by Toko on 17,October,2020
 **/

class UserProfileFragment : BaseFragment(), UserProfileViewMvc.Listener,
    UserProfileViewModel.Listener {

    private lateinit var mViewMvc: UserProfileViewMvc
    private lateinit var mViewModel: UserProfileViewModel

    private lateinit var argUserId: String
    private lateinit var argUserImageUri: String
    private lateinit var argUserUsername: String

    private var following = false
    private var followUnFollowClicked = false

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mScreensNavigator: UserProfileScreensNavigator
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()
        // TODO: 10/17/2020 implement refresh and error handling

        mViewMvc = mViewMvcFactory.newInstance(UserProfileViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = UserProfileFragmentArgs.fromBundle(requireArguments())
        argUserId = args.userId
        argUserImageUri = args.userImageUri
        argUserUsername = args.userUsername

        mViewMvc.bindProfileImage(argUserImageUri)
        mViewMvc.bindUsername(argUserUsername)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(UserProfileViewModel::class.java)

        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)

        mViewModel.fetchIfUserFollowsAndNotify(argUserId)

        val userPosts = mViewModel.getUserPosts()
        val userExtraInfo = mViewModel.getUserExtraInfo()
        val userStories = mViewModel.getUserStories()

        if (userExtraInfo != null) {
            onUserExtraInfoFetched(userExtraInfo)
        } else {
            mViewModel.fetchUserExtraInfoAndNotify(argUserId)
        }

        if (userPosts != null) {
            onPostsFetched(userPosts)
        } else {
            mViewModel.fetchUserPostsAndNotify(argUserId)
        }

        if (userStories.isNotEmpty()) {
            onStoriesFetched(userStories)
        } else {
            mViewModel.fetchNextStoriesPageAndNotify(argUserId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onFollowClicked() {
        followUnFollowClicked = true
        mViewModel.fetchIfUserFollowsAndNotify(argUserId)
    }

    override fun onSendMessageClicked() {
        mScreensNavigator.toChat(argUserId, argUserImageUri, argUserUsername)
    }

    override fun onStoryItemClicked(story: Story) {
        Log.d(javaClass.simpleName, "onStoryItemClicked() called with: storyModel = $story")
    }

    override fun onPostItemClicked(post: Post) {
        Log.d(javaClass.simpleName, "onPostItemClicked() called with: postModel = $post")
    }

    override fun onFollowerClicked() {
        mScreensNavigator.toDataPresenter(DataType.FOLLOWERS, argUserId)
    }

    override fun onFollowingClicked() {
        mScreensNavigator.toDataPresenter(DataType.FOLLOWING, argUserId)
    }

    override fun onLastStoryBound() {
        mViewModel.fetchNextStoriesPageAndNotify(argUserId)
    }

    // view model callbacks
    override fun onPostsFetched(posts: List<Post>) {
        if (posts.isNotEmpty()) {
            mViewMvc.bindPosts(posts)
        } else {
            mViewMvc.showNoPostsAvailable()
        }
    }

    override fun onUserExtraInfoFetched(userExtraInfo: UserExtraInfo) {
        mViewMvc.bindBio(userExtraInfo.bio)
        mViewMvc.bindPostCount(userExtraInfo.postCount)
        mViewMvc.bindFollowerCount(userExtraInfo.followerCount)
        mViewMvc.bindFollowingCount(userExtraInfo.followingCount)
    }

    override fun onFollowUnFollowCompleted() {
        following = !following
        mViewMvc.setFollowingButtonText(
            if (following) getString(R.string.following)
            else getString(R.string.follow)
        )
    }

    override fun onFollowResultReceived(following: Boolean) {
        this.following = following

        if (following) {
            if (followUnFollowClicked) {
                followUnFollowClicked = false
                mViewModel.unFollowUserAndNotify(argUserId)
            } else {
                mViewMvc.setFollowingButtonText(getString(R.string.following))
            }
        } else {
            if (followUnFollowClicked) {
                followUnFollowClicked = false
                mViewModel.followUserAndNotify(argUserId)
            } else {
                mViewMvc.setFollowingButtonText(getString(R.string.follow))
            }
        }
    }

    override fun onStoriesFetched(stories: List<Story>) {
        mViewMvc.addStories(stories)
    }
}