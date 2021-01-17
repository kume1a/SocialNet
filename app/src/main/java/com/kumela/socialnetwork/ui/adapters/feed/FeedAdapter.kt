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
    private val onBottomScrolledListener: () -> Unit
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>(), FeedItemViewMvc.Listener {

    interface Listener {
        fun onUserProfileOrUsernameClicked(user: User)
        fun onLikeClicked(position: Int, feedModel: Feed)
        fun onPostDoubleClicked(position: Int, feedModel: Feed)
        fun onLikeCountClicked(postId: String)
        fun onCommentClicked(postId: String)
    }

    private val items = arrayListOf<Feed>()

    fun addPost(post: Feed) {
        items.add(post)
        notifyItemInserted(items.size - 1)
    }

    fun bindPosts(posts: List<Feed>) {
        items.addAll(posts)
        notifyDataSetChanged()
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

        if (position == items.size - 1) {
            onBottomScrolledListener.invoke()
        }
    }

    override fun getItemCount(): Int = items.size

    class FeedViewHolder(val viewMvc: FeedItemViewMvc) : RecyclerView.ViewHolder(viewMvc.rootView)

    // view callbacks
    override fun onProfileImageClicked(position: Int, feedModel: Feed) {
        val user =
            User(feedModel.posterUid, feedModel.posterUsername, feedModel.posterImageUri)
        listener.onUserProfileOrUsernameClicked(user)
    }

    override fun onUsernameClicked(position: Int, feedModel: Feed) {
        val user =
            User(feedModel.posterUid, feedModel.posterUsername, feedModel.posterImageUri)
        listener.onUserProfileOrUsernameClicked(user)
    }

    override fun onLikeClicked(position: Int, feedModel: Feed) {
        listener.onLikeClicked(position, feedModel)
    }

    override fun onPostDoubleClicked(position: Int, feedModel: Feed) {
        listener.onPostDoubleClicked(position, feedModel)
    }

    override fun onLikeCountClicked(position: Int, feedModel: Feed) {
        listener.onLikeCountClicked(feedModel.postId)
    }

    override fun onCommentsClicked(position: Int, feedModel: Feed) {
        listener.onCommentClicked(feedModel.postId)
    }

    override fun onCommentCountClicked(position: Int, feedModel: Feed) {
        listener.onCommentClicked(feedModel.postId)
    }

    override fun onMenuClicked() {
        Log.d(javaClass.simpleName, "onMenuClicked() called")
    }
}