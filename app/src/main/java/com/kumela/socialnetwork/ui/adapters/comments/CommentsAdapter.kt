package com.kumela.socialnetwork.ui.adapters.comments

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.models.list.CommentListModel
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: Listener
) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>(), CommentsItemViewMvc.Listener {

    fun interface Listener {
        fun onUserClicked(userModel: UserModel)
    }

    private val items = ArrayList<CommentListModel>()

    fun bindComments(comments: List<CommentListModel>) {
        items.clear()
        items.addAll(comments)
        notifyDataSetChanged()
    }

    fun addComment(comment: CommentListModel) {
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
    override fun onUserProfileImageClicked(data: CommentListModel) {
        listener.onUserClicked(UserModel(data.posterId, data.posterUsername, data.posterImageUri))
    }

    override fun onUserUsernameClicked(data: CommentListModel) {
        listener.onUserClicked(UserModel(data.posterId, data.posterUsername, data.posterImageUri))
    }

    override fun onMenuClicked(data: CommentListModel) {
        Log.d(javaClass.simpleName, "onMenuClicked() called with: commentListModel = $data")
    }
}