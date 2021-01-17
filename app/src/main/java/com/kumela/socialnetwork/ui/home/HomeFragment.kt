package com.kumela.socialnetwork.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.ActivityResultListener
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.controllers.IntentDispatcher
import com.kumela.socialnetwork.ui.common.controllers.RequestResultDispatcher
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import com.kumela.socialnetwork.ui.story_presenter.StoryViewModel
import com.kumela.socialnetwork.ui.user_list.DataType
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class HomeFragment : BaseFragment(), HomeViewMvc.Listener,
    HomeViewModel.Listener, ActivityResultListener, StoryViewModel.Listener {

    private lateinit var mViewMvc: HomeViewMvc
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mStoryViewModel: StoryViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: HomeScreensNavigator
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mIntentDispatcher: IntentDispatcher
    @Inject lateinit var mRequestResultDispatcher: RequestResultDispatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mViewMvc = mViewMvcFactory.newInstance(HomeViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBottomNavHelper.showBottomNav()
        mViewModel = ViewModelProvider(requireActivity(), mViewModelFactory).get(HomeViewModel::class.java)
        mStoryViewModel = ViewModelProvider(requireActivity()).get(StoryViewModel::class.java)

        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)
        mStoryViewModel.registerListener(this)
        mRequestResultDispatcher.registerRequestResultListener(this)

//        val posts = mViewModel.getPosts()
//        val userModel = mViewModel.getUser()
//
//        if (posts.isNotEmpty()) {
//            mViewMvc.bindPosts(posts)
//        } else {
//            mViewModel.fetchNextPostAndNotify()
//        }
//
//        if (userModel != null) {
//            val storyPosterUsers = mStoryViewModel.getStoryPosters()
//            if (storyPosterUsers.isNotEmpty()) {
//                val stories = ArrayList<UserModel>()
//                stories.add(userModel)
//                stories.addAll(storyPosterUsers)
//                mViewMvc.bindStories(stories)
//            } else {
//                mViewMvc.addStory(userModel)
//                mStoryViewModel.fetchStoryPostersAndNotify()
//            }
//        } else {
//            mViewModel.fetchUserAndNotify()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
        mStoryViewModel.unregisterListener(this)
        mRequestResultDispatcher.unregisterRequestResultListener(this)
    }

    override fun onStoryClicked(position: Int, user: User) {
        if (position == 0) {
            mIntentDispatcher.dispatchImagePickerIntent(REQUEST_PICK_STORY_IMAGE)
        } else {
            mScreensNavigator.toStoryPresenter(user.id)
        }
    }

    override fun onScrolledToBottom() {
        mViewModel.fetchNextPostAndNotify()
    }

    override fun onUserFetched(user: User) {
        mViewMvc.addStory(user)
        mStoryViewModel.fetchStoryPostersAndNotify()
    }

    override fun onPostFetched(feedModel: Feed) {
        mViewMvc.addPost(feedModel)
    }

    override fun onPostUpdated(position: Int, feedModel: Feed) {
        mViewMvc.updatePost(position, feedModel)
    }

    // feed item view callbacks
    override fun onUserProfileOrUsernameClicked(user: User) {
        mScreensNavigator.toUserProfile(user.id, user.imageUri, user.username)
    }

    override fun onLikeClicked(position: Int, feedModel: Feed) {
        mViewModel.likeOrDislikePostAndNotify(position, feedModel)
    }

    override fun onPostDoubleClick(position: Int, feedModel: Feed) {
        mViewModel.likeOrDislikePostAndNotify(position, feedModel)
    }

    override fun onLikeCountClicked(postId: String) {
        mScreensNavigator.toUsersList(DataType.LIKES, postId)
    }

    override fun onCommentClicked(postId: String) {
        val user = mViewModel.getUser()
        if (user != null) {
            mScreensNavigator.toComments(postId, user.id, user.imageUri, user.username)
        }
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

    companion object {
        private const val REQUEST_PICK_STORY_IMAGE = 8987
    }
}