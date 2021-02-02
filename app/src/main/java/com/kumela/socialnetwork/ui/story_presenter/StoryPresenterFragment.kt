package com.kumela.socialnetwork.ui.story_presenter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import javax.inject.Inject

/**
 * Created by Toko on 09,November,2020
 **/

class StoryPresenterFragment : BaseFragment(), StoryPresenterViewMvc.Listener {

    private lateinit var mViewMvc: StoryPresenterViewMvc
    private lateinit var mViewModel: StoryPresenterViewModel

    private var currentUserId: Int = -1
    private var currentImageIndex = 0
    private var lastPressTs = 0L

    @Inject lateinit var mScreensNavigator: StoryPresenterScreensNavigator
    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(StoryPresenterViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = StoryPresenterFragmentArgs.fromBundle(requireArguments())

        currentUserId = args.initialAuthorId

        mViewModel = ViewModelProvider(requireActivity()).get(StoryPresenterViewModel::class.java)

        lifecycleScope.launchWhenStarted {
            fetchUserStoriesAndBind()
        }

        mViewMvc. registerListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
    }

    override fun onCloseClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onUserClicked() {
        Log.d(javaClass.simpleName, "onUserClicked() called")
    }

    override fun onReverse() {
        mViewMvc.reverse()
    }

    override fun onSkip() {
        mViewMvc.skip()
    }

    override fun onPressDown(): Boolean {
        mViewMvc.pause()
        lastPressTs = System.currentTimeMillis()
        return false
    }

    override fun onPressUp(): Boolean {
        mViewMvc.resume()
        return 500L < System.currentTimeMillis() - lastPressTs
    }

    override fun onNext() {
        val stories = mViewModel.getStories(currentUserId)
        if (stories != null) {
            mViewMvc.bindImage(stories[++currentImageIndex].imageUrl)
        }
    }

    override fun onPrevious() {
        if (currentImageIndex - 1 < 0) return

        val stories = mViewModel.getStories(currentUserId)
        if (stories != null) {
            mViewMvc.bindImage(stories[--currentImageIndex].imageUrl)
        }
    }

    override fun onBack() {
        val users = mViewModel.getCachedStoryAuthors()!!.data
        if (users.first().id != currentUserId) {
            currentImageIndex = 0
            currentUserId = users[users.indexOfFirst { it.id == currentUserId } - 1].id
            lifecycleScope.launchWhenStarted {
                fetchUserStoriesAndBind()
            }
        }
    }

    override fun onComplete() {
        val users = mViewModel.getCachedStoryAuthors()!!.data
        if (users.last().id == currentUserId) {
            mScreensNavigator.navigateUp()
        } else {
            currentImageIndex = 0
            currentUserId = users[users.indexOfFirst { it.id == currentUserId } + 1].id
            lifecycleScope.launchWhenStarted {
                fetchUserStoriesAndBind()
            }
        }
    }

    private suspend fun fetchUserStoriesAndBind() {
        val result = mViewModel.fetchStories(currentUserId)
        result.fold(
            onSuccess = { stories ->
                val user =
                    mViewModel.getCachedStoryAuthors()!!.data.first { it.id == currentUserId }

                mViewMvc.bindUser(user)
                mViewMvc.bindImage(stories.first().imageUrl)
                mViewMvc.bindCount(stories.size)
                mViewMvc.start()
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchUserStoriesAndBind: $error")
            }
        )
    }
}