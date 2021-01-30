package com.kumela.socialnetwork.ui.adapters.comments

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: Listener
) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>(), CommentsItemViewMvc.Listener {

    interface Listener {
        fun onUserClicked(user: User)
        fun onLikeClicked(comment: Comment)
        fun onReplyClicked(comment: Comment)
        fun onReplierClicked(comment: Comment)

        fun onLastCommentBound()
    }

    private val items = ArrayList<Comment>()

    fun addComments(comments: List<Comment>) {
        items.addAll(0, comments)
        notifyItemRangeInserted(0, items.size)
    }

    fun addComment(comment: Comment) {
        items.add(0, comment)
        notifyItemInserted(0)
    }

    fun updateComment(comment: Comment) {
        val index = items.indexOfFirst { it.id == comment.id }
        items[index] = comment
        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val viewMvc = viewMvcFactory.newInstance(CommentsItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return CommentsViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        if (position == items.size -1) {
            listener.onLastCommentBound()
        }
        holder.viewMvc.bindComment(items[position])
    }

    override fun getItemCount(): Int = items.size

    class CommentsViewHolder(val viewMvc: CommentsItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    // view callbacks
    override fun onUserProfileImageClicked(comment: Comment) {
        listener.onUserClicked(User(comment.userId, comment.userName, comment.userImageUrl))
    }

    override fun onUserNameClicked(comment: Comment) {
        listener.onUserClicked(User(comment.userId, comment.userName, comment.userImageUrl))
    }

    override fun onLikeClicked(comment: Comment) {
        listener.onLikeClicked(comment)
    }

    override fun onReplyClicked(comment: Comment) {
        listener.onReplyClicked(comment)
    }

    override fun onReplierClicked(comment: Comment) {
        listener.onReplierClicked(comment)
    }
}