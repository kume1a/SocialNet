package com.kumela.socialnetwork.ui.user_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.network.repositories.FollowRepository
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Toko on 17,October,2020
 **/

class UserProfileFragment : BaseFragment(), UserProfileViewMvc.Listener {

    private lateinit var mViewMvc: UserProfileViewMvc

    private var argId: Int = -1
    private lateinit var argImageUri: String
    private lateinit var argName: String
    private lateinit var argBio: String

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: UserProfileScreensNavigator
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mFollowRepository: FollowRepository

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
        argBio = args.bio

        mViewMvc.bindProfileImage(argImageUri)
        mViewMvc.bindUsername(argName)

        mViewMvc.registerListener(this)

        lifecycleScope.launch {
            val followStatusResult = mFollowRepository.fetchFollowStatus(argId)
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
//        val userPosts = mViewModel.getUserPosts()
//        val userExtraInfo = mViewModel.getUserExtraInfo()
//        val userStories = mViewModel.getUserStories()
//
//        if (userExtraInfo != null) {
//            onUserExtraInfoFetched(userExtraInfo)
//        } else {
//            mViewModel.fetchUserExtraInfoAndNotify(argId)
//        }
//
//        if (userPosts != null) {
//            onPostsFetched(userPosts)
//        } else {
//            mViewModel.fetchUserPostsAndNotify(argId)
//        }
//
//        if (userStories.isNotEmpty()) {
//            onStoriesFetched(userStories)
//        } else {
//            mViewModel.fetchNextStoriesPageAndNotify(argId)
//        }
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
            mFollowRepository.switchFollowStatus(argId)

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

    override fun onLastStoryBound() {
//        mViewModel.fetchNextStoriesPageAndNotify(argId)
    }

    // view model callbacks
//    override fun onPostsFetched(posts: List<Post>) {
//        if (posts.isNotEmpty()) {
//            mViewMvc.bindPosts(posts)
//        } else {
//            mViewMvc.showNoPostsAvailable()
//        }
//    }
//
//    override fun onUserExtraInfoFetched(userExtraInfo: UserExtraInfo) {
//        mViewMvc.bindBio(userExtraInfo.bio)
//        mViewMvc.bindPostCount(userExtraInfo.postCount)
//        mViewMvc.bindFollowerCount(userExtraInfo.followerCount)
//        mViewMvc.bindFollowingCount(userExtraInfo.followingCount)
//    }
//
//    override fun onStoriesFetched(stories: List<Story>) {
//        mViewMvc.addStories(stories)
//    }
}