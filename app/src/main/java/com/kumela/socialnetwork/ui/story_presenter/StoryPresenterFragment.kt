package com.kumela.socialnetwork.ui.story_presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.models.FeedStoryModel
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import javax.inject.Inject

/**
 * Created by Toko on 09,November,2020
 **/

class StoryPresenterFragment : BaseFragment(), StoryPresenterViewMvc.Listener {

    private lateinit var mViewMvc: StoryPresenterViewMvc
    private lateinit var mViewModel: StoryViewModel

    private lateinit var argInitialAuthorId: String

    private var mLastImagePage = 0
    private var mLastStoryPage = 0
    private val mAllStories = ArrayList<FeedStoryModel>()

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

        argInitialAuthorId = args.initialAuthorId

        mViewModel = ViewModelProvider(requireActivity()).get(StoryViewModel::class.java)

        mViewMvc.registerListener(this)

        val feedStories = mViewModel.getFeedStories()
        val feedStoryAuthors = mViewModel.getStoryPosters()

        if (feedStories != null && feedStoryAuthors.isNotEmpty()) {
            // populate all stories list
            mAllStories.clear()
            feedStories.forEach { mAllStories.addAll(it.stories) }

            // find initial feed stories object and it's index
            val initialFeedStories = feedStories.first { it.userId == argInitialAuthorId }
            val initialFeedStoriesIndex = feedStories.indexOf(initialFeedStories)

            // assign initial values for control variables
            mLastStoryPage = initialFeedStoriesIndex
            mLastImagePage = mAllStories.indexOf(initialFeedStories.stories[0])

            // bind all images to image pager
            mViewMvc.bindStories(mAllStories)

            // bind initial story author
//            mViewMvc.bindStoryAuthor(feedStoryAuthors.first { it.id == argInitialAuthorId })

            // bind image count for indicator
            mViewMvc.bindImageCount(initialFeedStories.stories.size, 0)

            // move to initial image
            mViewMvc.imageIndexTo(mLastImagePage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
    }

    override fun onCloseClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onPageChanged(position: Int) {
        val feedStories = mViewModel.getFeedStories()!!
        val storyPage = feedStories.indexOfFirst { it.stories.contains(mAllStories[position]) }

        if (storyPage != mLastStoryPage) {
            val currentFeedStory = feedStories[storyPage]

            // change story author and reset indicator
//            mViewMvc.bindStoryAuthor(
//                mViewModel.getStoryPosters().first { it.id == currentFeedStory.userId })
            mViewMvc.bindImageCount(
                currentFeedStory.stories.size,
                if (mLastStoryPage > storyPage) currentFeedStory.stories.size - 1 else 0
            )
        } else {
            // change the indicator index
            if (position > mLastImagePage) {
                mViewMvc.nextIndex()
            } else if (position < mLastImagePage) {
                mViewMvc.previousIndex()
            }
        }

        mLastStoryPage = storyPage
        mLastImagePage = position
    }
}