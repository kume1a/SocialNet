package com.kumela.socialnetwork.ui.adapters.comments

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.CommentList
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: Listener
) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>(), CommentsItemViewMvc.Listener {

    fun interface Listener {
        fun onUserClicked(user: User)
    }

    private val items = ArrayList<CommentList>()

    fun bindComments(comments: List<CommentList>) {
        items.clear()
        items.addAll(comments)
        notifyDataSetChanged()
    }

    fun addComment(comment: CommentList) {
        items.add(0, comment)
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val viewMvc = viewMvcFactory.newInstance(CommentsItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return CommentsViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.viewMvc.bindComment(items[position])
    }

    override fun getItemCount(): Int = items.size

    class CommentsViewHolder(val viewMvc: CommentsItemViewMvc) : RecyclerView.ViewHolder(viewMvc.rootView)

    // view callbacks
    override fun onUserProfileImageClicked(data: CommentList) {
        listener.onUserClicked(User(data.posterId, data.posterUsername, data.posterImageUri))
    }

    override fun onUserUsernameClicked(data: CommentList) {
        listener.onUserClicked(User(data.posterId, data.posterUsername, data.posterImageUri))
    }

    override fun onMenuClicked(data: CommentList) {
        Log.d(javaClass.simpleName, "onMenuClicked() called with: commentListModel = $data")
    }
}