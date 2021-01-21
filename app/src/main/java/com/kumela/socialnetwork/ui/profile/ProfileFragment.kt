package com.kumela.socialnetwork.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.authentication.AuthUseCase
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.common.fold
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

class ProfileFragment : BaseFragment(), ProfileViewMvc.Listener {

    private lateinit var mViewMvc: ProfileViewMvc
    private lateinit var mViewModel: ProfileViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mScreensNavigator: ProfileScreensNavigator
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mAuthUseCase: AuthUseCase
    @Inject lateinit var keyStore: KeyStore

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
        lifecycleScope.launchWhenStarted {
            fetchUser()

            val posts = mViewModel.getPosts()
            if (posts != null) {
                mViewMvc.addPosts(posts.data)
            } else {
                fetchPosts()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mViewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        mViewMvc.unregisterListener()
    }

    override fun onStoryItemClicked(story: Story) {
        Log.d(javaClass.simpleName, "onStoryItemClicked() called with: storyModel = $story")
    }

    override fun onPostItemClicked(post: Post) {
        Log.d(javaClass.simpleName, "onPostItemClicked() called with: postModel = $post")
    }

    override fun onSignOutClicked() {
        activity?.viewModelStore?.clear()
        mAuthUseCase.signout()
        mBottomNavHelper.resetIndex()
        mScreensNavigator.toAuth()
    }

    override fun onFollowerClicked() {
        mScreensNavigator.toUsersList(DataType.FOLLOWERS, UserUseCase.uid)
    }

    override fun onFollowingClicked() {
        mScreensNavigator.toUsersList(DataType.FOLLOWING, UserUseCase.uid)
    }

    override fun onLastPostBound() {
        lifecycleScope.launchWhenStarted {
            fetchPosts()
        }
    }

    override fun onLastStoryBound() {
//        mViewModel.fetchNextStoriesPageAndNotify()
    }

    private suspend fun fetchUser() {
        val userResult = mViewModel.fetchUser()
        userResult.fold(
            onSuccess = { user ->
                mViewMvc.bindProfileImage(user.imageUrl)
                mViewMvc.bindUsername(user.name)
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchUser: $error")
            }
        )
    }

    private suspend fun fetchPosts() {
        val result = mViewModel.fetchPosts(keyStore.getUserId())
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