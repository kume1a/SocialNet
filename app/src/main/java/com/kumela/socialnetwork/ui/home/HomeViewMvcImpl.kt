package com.kumela.socialnetwork.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.adapters.feed.FeedAdapter
import com.kumela.socialnetwork.ui.adapters.story.StoryAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

class HomeViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<HomeViewMvc.Listener>(
    inflater, parent, R.layout.fragment_home
), HomeViewMvc, FeedAdapter.Listener {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val recyclerFeed: RecyclerView = findViewById(R.id.recycler_feed)
    private val recyclerStory: RecyclerView = findViewById(R.id.recycler_stories)
    private val feedAdapter = FeedAdapter(viewMvcFactory, this)
    private val storyAdapter = StoryAdapter(viewMvcFactory) { position, userModel ->
        listener?.onStoryClicked(position, userModel)
    }

    init {
        toolbar.addView(toolbarViewMvc.rootView)
        toolbarViewMvc.setTitle("Social Net")

        val scrollView = findViewById<NestedScrollView>(R.id.scroll_view)
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val view = scrollView.getChildAt(scrollView.childCount - 1)
            val diff = (view.bottom - (scrollView.height + scrollView.scrollY))

            if (diff == 0) listener?.onScrolledToBottom()
        }

        recyclerStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
        }

        recyclerFeed.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
        }
    }

    override fun bindStories(users: List<User>) {
        storyAdapter.bindStories(users)
    }

    override fun addStory(user: User) {
        storyAdapter.addStory(user)
    }

    override fun addPosts(posts: List<Feed>) {
        feedAdapter.addPosts(posts)
    }

    override fun updatePost(position: Int, feed: Feed) {
        feedAdapter.updatePost(position, feed)
    }

    // feed adapter view callbacks
    override fun onUserProfileOrUsernameClicked(user: User) {
        listener?.onUserProfileOrUsernameClicked(user)
    }

    override fun onLikeClicked(position: Int, feed: Feed) {
        listener?.onLikeClicked(position, feed)
    }

    override fun onPostDoubleClicked(position: Int, feed: Feed) {
        listener?.onPostDoubleClick(position, feed)
    }

    override fun onLikeCountClicked(postId: Int) {
        listener?.onLikeCountClicked(postId)
    }

    override fun onCommentClicked(postId: Int) {
        listener?.onCommentClicked(postId)
    }
}
