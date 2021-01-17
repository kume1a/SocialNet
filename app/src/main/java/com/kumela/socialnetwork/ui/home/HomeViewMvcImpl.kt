package com.kumela.socialnetwork.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
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
    private val feedAdapter = FeedAdapter(viewMvcFactory, this) {
        listener?.onScrolledToBottom()
    }
    private val storyAdapter = StoryAdapter(viewMvcFactory) { position, userModel ->
        listener?.onStoryClicked(position, userModel)
    }

    init {
        toolbar.addView(toolbarViewMvc.rootView)
        toolbarViewMvc.setTitle("Social Net")

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

    override fun getCurrentFeedListOffset(): Int {
        return recyclerFeed.computeVerticalScrollOffset()
    }

    override fun scrollFeedListTo(feedListOffset: Int) {
        recyclerFeed.scrollToPosition(feedListOffset)
    }

    override fun bindStories(users: List<User>) {
        storyAdapter.bindStories(users)
    }

    override fun addStory(user: User) {
        storyAdapter.addStory(user)
    }

    override fun addPost(post: Feed) {
        feedAdapter.addPost(post)
    }

    override fun bindPosts(posts: List<Feed>) {
        feedAdapter.bindPosts(posts)
    }

    override fun updatePost(position: Int, feedModel: Feed) {
        feedAdapter.updatePost(position, feedModel)
    }

    // feed adapter view callbacks
    override fun onUserProfileOrUsernameClicked(user: User) {
        listener?.onUserProfileOrUsernameClicked(user)
    }

    override fun onLikeClicked(position: Int, feedModel: Feed) {
        listener?.onLikeClicked(position, feedModel)
    }

    override fun onPostDoubleClicked(position: Int, feedModel: Feed) {
        listener?.onPostDoubleClick(position, feedModel)
    }

    override fun onLikeCountClicked(postId: String) {
        listener?.onLikeCountClicked(postId)
    }

    override fun onCommentClicked(postId: String) {
        listener?.onCommentClicked(postId)
    }
}
