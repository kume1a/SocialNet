package com.kumela.socialnetwork.ui.adapters.feed

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.firebase.FeedModel
import com.kumela.socialnetwork.models.firebase.UserModel
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
        fun onUserProfileOrUsernameClicked(userModel: UserModel)
        fun onLikeClicked(position: Int, feedModel: FeedModel)
        fun onPostDoubleClicked(position: Int, feedModel: FeedModel)
        fun onLikeCountClicked(postId: String)
        fun onCommentClicked(postId: String)
    }

    private val items = arrayListOf<FeedModel>()

    fun addPost(post: FeedModel) {
        items.add(post)
        notifyItemInserted(items.size - 1)
    }

    fun bindPosts(posts: List<FeedModel>) {
        items.addAll(posts)
        notifyDataSetChanged()
    }

    fun updatePost(position: Int, post: FeedModel) {
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
    override fun onProfileImageClicked(position: Int, feedModel: FeedModel) {
        val user =
            UserModel(feedModel.posterUid, feedModel.posterUsername, feedModel.posterImageUri)
        listener.onUserProfileOrUsernameClicked(user)
    }

    override fun onUsernameClicked(position: Int, feedModel: FeedModel) {
        val user =
            UserModel(feedModel.posterUid, feedModel.posterUsername, feedModel.posterImageUri)
        listener.onUserProfileOrUsernameClicked(user)
    }

    override fun onLikeClicked(position: Int, feedModel: FeedModel) {
        listener.onLikeClicked(position, feedModel)
    }

    override fun onPostDoubleClicked(position: Int, feedModel: FeedModel) {
        listener.onPostDoubleClicked(position, feedModel)
    }

    override fun onLikeCountClicked(position: Int, feedModel: FeedModel) {
        listener.onLikeCountClicked(feedModel.postId)
    }

    override fun onCommentsClicked(position: Int, feedModel: FeedModel) {
        listener.onCommentClicked(feedModel.postId)
    }

    override fun onCommentCountClicked(position: Int, feedModel: FeedModel) {
        listener.onCommentClicked(feedModel.postId)
    }

    override fun onMenuClicked() {
        Log.d(javaClass.simpleName, "onMenuClicked() called")
    }
}