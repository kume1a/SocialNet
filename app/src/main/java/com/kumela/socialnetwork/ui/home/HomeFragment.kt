package com.kumela.socialnetwork.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.ActivityResultListener
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.controllers.IntentDispatcher
import com.kumela.socialnetwork.ui.common.controllers.RequestResultDispatcher
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import com.kumela.socialnetwork.ui.story_presenter.StoryViewModel
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class HomeFragment : BaseFragment(), HomeViewMvc.Listener,
    ActivityResultListener, StoryViewModel.Listener {

    private lateinit var mViewMvc: HomeViewMvc
    private lateinit var mViewModel: HomeViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mScreensNavigator: HomeScreensNavigator
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mIntentDispatcher: IntentDispatcher
    @Inject lateinit var mRequestResultDispatcher: RequestResultDispatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.showBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(HomeViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewMvc.registerListener(this)
        mRequestResultDispatcher.registerRequestResultListener(this)

        mViewModel =
            ViewModelProvider(requireActivity(), mViewModelFactory).get(HomeViewModel::class.java)

        val cachedFeedPosts = mViewModel.getCachedFeedPosts()
        lifecycleScope.launchWhenStarted {
            if (cachedFeedPosts != null) {
                mViewMvc.addPosts(cachedFeedPosts.data)
            } else {
                fetchFeedPosts()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mRequestResultDispatcher.unregisterRequestResultListener(this)
    }

    override fun onStoryClicked(position: Int, user: User) {
//        if (position == 0) {
//            mIntentDispatcher.dispatchImagePickerIntent(REQUEST_PICK_STORY_IMAGE)
//        } else {
//            mScreensNavigator.toStoryPresenter(user.id)
//        }
    }

    override fun onScrolledToBottom() {
        lifecycleScope.launchWhenStarted {
            fetchFeedPosts()
        }
    }

    // feed item view callbacks
    override fun onUserProfileOrUsernameClicked(user: User) {
        mScreensNavigator.toUserProfile(user.id, user.name, user.imageUrl, user.bio)
    }

    override fun onLikeClicked(position: Int, feed: Feed) {
        lifecycleScope.launchWhenStarted { likeOrDislikePost(position, feed) }
    }

    override fun onPostDoubleClick(position: Int, feed: Feed) {
        lifecycleScope.launchWhenStarted { likeOrDislikePost(position, feed) }
    }

    override fun onLikeCountClicked(postId: Int) {
        Log.d(javaClass.simpleName, "onLikeCountClicked() called with: postId = $postId")
    }

    override fun onCommentClicked(postId: Int) {
        Log.d(javaClass.simpleName, "onCommentClicked() called with: postId = $postId")
    }

    // story model callbacks
    override fun onStoryPosterFetched(user: User) {
        mViewMvc.addStory(user)
    }

    // request result dispatcher
    override fun onRequestResultReceived(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PICK_STORY_IMAGE && data != null) {
                if (data.dataString != null) {
                    val imageUri = Uri.parse(data.dataString)
                    mScreensNavigator.toStoryUploader(imageUri)
                }
            }
        }
    }

    private suspend fun fetchFeedPosts() {
        val result = mViewModel.fetchFeedPosts()
        result.fold(
            onSuccess = { response ->
                if (response == null) return@fold

                mViewMvc.addPosts(response.data)
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchFeedPosts: $error")
            }
        )
    }

    private suspend fun likeOrDislikePost(position: Int, feed: Feed) {
        val newFeed = if (feed.isLiked) {
            feed.copy(isLiked = false, likeCount = feed.likeCount - 1)
        } else {
            feed.copy(isLiked = true, likeCount = feed.likeCount + 1)
        }

        mViewMvc.updatePost(position, newFeed)

        if (feed.isLiked) {
            mViewModel.dislikePost(feed.id)
        } else {
            mViewModel.likePost(feed.id)
        }

        mViewModel.getCachedFeedPosts()?.data?.set(position, newFeed)
    }

    companion object {
        private const val REQUEST_PICK_STORY_IMAGE = 8987
    }
}