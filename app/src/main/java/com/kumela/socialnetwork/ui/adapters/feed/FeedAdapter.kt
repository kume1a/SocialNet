package com.kumela.socialnetwork.ui.adapters.feed

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 01,October,2020
 **/

class FeedAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: Listener,
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>(), FeedItemViewMvc.Listener {

    interface Listener {
        fun onUserProfileOrUsernameClicked(user: User)
        fun onLikeClicked(position: Int, feed: Feed)
        fun onPostDoubleClicked(position: Int, feed: Feed)
        fun onLikeCountClicked(postId: Int)
        fun onCommentClicked(postId: Int)
    }

    private val items = arrayListOf<Feed>()

    fun addPosts(posts: List<Feed>) {
        val size = itemCount
        items.addAll(posts)
        notifyItemRangeInserted(size, posts.size)
    }

    fun updatePost(position: Int, post: Feed) {
        items[position] = post
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val viewMvc = viewMvcFactory.newInstance(FeedItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return FeedViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.viewMvc.bindPost(position, items[position])
    }

    override fun getItemCount(): Int = items.size

    class FeedViewHolder(val viewMvc: FeedItemViewMvc) : RecyclerView.ViewHolder(viewMvc.rootView)

    // view callbacks
    override fun onProfileImageClicked(position: Int, feed: Feed) {
        val user = User(feed.userId, feed.userName, feed.userImageUrl, feed.userBio)
        listener.onUserProfileOrUsernameClicked(user)
    }

    override fun onUsernameClicked(position: Int, feed: Feed) {
        val user = User(feed.userId, feed.userName, feed.userImageUrl, feed.userBio)
        listener.onUserProfileOrUsernameClicked(user)
    }

    override fun onLikeClicked(position: Int, feed: Feed) {
        listener.onLikeClicked(position, feed)
    }

    override fun onPostDoubleClicked(position: Int, feed: Feed) {
        listener.onPostDoubleClicked(position, feed)
    }

    override fun onLikeCountClicked(position: Int, feed: Feed) {
        listener.onLikeCountClicked(feed.id)
    }

    override fun onCommentsClicked(position: Int, feed: Feed) {
        listener.onCommentClicked(feed.id)
    }

    override fun onCommentCountClicked(position: Int, feed: Feed) {
        listener.onCommentClicked(feed.id)
    }

    override fun onMenuClicked() {
        Log.d(javaClass.simpleName, "onMenuClicked() called")
    }
}