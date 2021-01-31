package com.kumela.socialnetwork.ui.user_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Toko on 17,October,2020
 **/

class UserProfileFragment : BaseFragment(), UserProfileViewMvc.Listener {

    private lateinit var mViewMvc: UserProfileViewMvc
    private lateinit var mViewModel: UserProfileViewModel

    private var argId: Int = -1
    private lateinit var argImageUri: String
    private lateinit var argName: String

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
        argId = args.id
        argImageUri = args.imageUrl
        argName = args.name

        mViewMvc.bindProfileImage(argImageUri)
        mViewMvc.bindUsername(argName)

        mViewMvc.registerListener(this)
        mViewModel =
            ViewModelProvider(this, mViewModelFactory).get(UserProfileViewModel::class.java)

        lifecycleScope.launch {
            fetchFollowStatus()
            fetchUserMeta()

            val posts = mViewModel.getPosts()
            if (posts != null) {
                mViewMvc.addPosts(posts.data)
            } else {
                fetchPosts()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
    }

    override fun onNavigateUpClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onFollowClicked() {
        lifecycleScope.launchWhenStarted {
            mViewModel.switchFollowStatus(argId)

            val following = mViewMvc.getFollowingButtonText() == getString(R.string.following)
            mViewMvc.setFollowingButtonText(
                getString(
                    if (following) R.string.follow
                    else R.string.following
                )
            )
        }
    }

    override fun onSendMessageClicked() {
//        mScreensNavigator.toChat(argId, argImageUri, argName)
    }

    override fun onStoryItemClicked(story: Story) {
        Log.d(javaClass.simpleName, "onStoryItemClicked() called with: storyModel = $story")
    }

    override fun onPostItemClicked(post: Post) {
        Log.d(javaClass.simpleName, "onPostItemClicked() called with: postModel = $post")
    }

    override fun onFollowerClicked() {
//        mScreensNavigator.toDataPresenter(DataType.FOLLOWERS, argId)
    }

    override fun onFollowingClicked() {
//        mScreensNavigator.toDataPresenter(DataType.FOLLOWING, argId)
    }

    override fun onLastPostBound() {
        lifecycleScope.launchWhenStarted {
            fetchPosts()
        }
    }

    override fun onLastStoryBound() {
//        mViewModel.fetchNextStoriesPageAndNotify(argId)
    }

    private suspend fun fetchFollowStatus() {
        val followStatusResult = mViewModel.fetchFollowStatus(argId)
        followStatusResult.fold(
            onSuccess = { follows ->
                mViewMvc.setFollowingButtonText(
                    getString(
                        if (follows) R.string.following
                        else R.string.follow
                    )
                )
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "onViewCreated: $error")
            },
        )
    }

    private suspend fun fetchUserMeta() {
        val result = mViewModel.fetchUserMeta(argId)
        result.fold(
            onSuccess = { userMeta ->
                mViewMvc.bindPostCount(userMeta.postCount)
                mViewMvc.bindFollowerCount(userMeta.followerCount)
                mViewMvc.bindFollowingCount(userMeta.followingCount)
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchUserMeta: $error")
            }
        )
    }

    private suspend fun fetchPosts() {
        val result = mViewModel.fetchPosts(argId)
        Log.d(javaClass.simpleName, "fetchPosts: $result")
        result.fold(
            onSuccess = { response ->
                if (response == null) return@fold

                if (response.data.isNotEmpty()) {
                    mViewMvc.addPosts(response.data)
                } else {
                    if (response.page == 1) {
                        mViewMvc.showNoPostsAvailable()
                    }
                }
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchPosts: $error")
            }
        )
    }
}