package com.kumela.socialnetwork.ui.adapters.reply

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.Reply
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

class ReplyAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: Listener
) : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>(), ReplyItemViewMvc.Listener {

    interface Listener {
        fun onUserClicked(user: User)
        fun onLikeClicked(reply: Reply)

        fun onLastReplyBound()
    }

    private val items = ArrayList<Reply>()

    fun addReplies(replies: List<Reply>) {
        items.addAll(0, replies)
        notifyItemRangeInserted(0, items.size)
    }

    fun addReply(reply: Reply) {
        items.add(0, reply)
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val viewMvc = viewMvcFactory.newInstance(ReplyItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return ReplyViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        if (position == items.size -1) {
            listener.onLastReplyBound()
        }
        holder.viewMvc.bindReply(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ReplyViewHolder(val viewMvc: ReplyItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    // view callbacks
    override fun onUserProfileImageClicked(reply: Reply) {
        listener.onUserClicked(User(reply.userId, reply.userName, reply.userImageUrl))
    }

    override fun onUserNameClicked(reply: Reply) {
        listener.onUserClicked(User(reply.userId, reply.userName, reply.userImageUrl))
    }

    override fun onLikeClicked(reply: Reply) {
        listener.onLikeClicked(reply)
    }
}
