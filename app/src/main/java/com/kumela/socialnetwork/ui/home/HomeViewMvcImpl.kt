package com.kumela.socialnetwork.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.firebase.FeedModel
import com.kumela.socialnetwork.models.firebase.UserModel
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

    override fun bindStories(users: List<UserModel>) {
        storyAdapter.bindStories(users)
    }

    override fun addStory(user: UserModel) {
        storyAdapter.addStory(user)
    }

    override fun addPost(post: FeedModel) {
        feedAdapter.addPost(post)
    }

    override fun bindPosts(posts: List<FeedModel>) {
        feedAdapter.bindPosts(posts)
    }

    override fun updatePost(position: Int, feedModel: FeedModel) {
        feedAdapter.updatePost(position, feedModel)
    }

    // feed adapter view callbacks
    override fun onUserProfileOrUsernameClicked(userModel: UserModel) {
        listener?.onUserProfileOrUsernameClicked(userModel)
    }

    override fun onLikeClicked(position: Int, feedModel: FeedModel) {
        listener?.onLikeClicked(position, feedModel)
    }

    override fun onPostDoubleClicked(position: Int, feedModel: FeedModel) {
        listener?.onPostDoubleClick(position, feedModel)
    }

    override fun onLikeCountClicked(postId: String) {
        listener?.onLikeCountClicked(postId)
    }

    override fun onCommentClicked(postId: String) {
        listener?.onCommentClicked(postId)
    }
}
