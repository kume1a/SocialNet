package com.kumela.socialnetwork.ui.adapters.messages

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.list.MessageListModel
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 08,October,2020
 **/

class MessageAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: Listener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), MessageLeftItemViewMvc.Listener {

    interface Listener {
        fun onScrolledToTop()
        fun onHeartClicked(messageModel: MessageListModel)
    }

    private val items = arrayListOf<MessageListModel>()
    private val uid = UserUseCase.uid

    fun addMessage(messageModel: MessageListModel) {
        items.add(0, messageModel)
        notifyItemInserted(0)
    }

    fun addMessages(messages: List<MessageListModel>) {
        val oldSize = items.size
        items.addAll(messages)
        notifyItemRangeInserted(oldSize, messages.size)
    }

    fun bindMessages(messageModels: List<MessageListModel>) {
        items.clear()
        items.addAll(messageModels)
        notifyDataSetChanged()
    }

    fun updateMessage(messageModel: MessageListModel) {
        val index = items.indexOfFirst { it.timestamp == messageModel.timestamp }
        items[index] = messageModel
//        notifyItemChanged(index)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].senderId != uid) {
            VIEW_TYPE_LEFT
        } else VIEW_TYPE_RIGHT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LEFT -> {
                val viewMvc = viewMvcFactory.newInstance(MessageLeftItemViewMvc::class, parent)
                viewMvc.registerListener(this)
                ChatViewHolderLeft(viewMvc)
            }
            VIEW_TYPE_RIGHT -> {
                val viewMvc = viewMvcFactory.newInstance(MessageRightItemViewMvc::class, parent)
                ChatViewHolderRight(viewMvc)
            }
            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == items.size - 1) {
            listener.onScrolledToTop()
        }

        when (holder.itemViewType) {
            VIEW_TYPE_LEFT -> {
                val viewMvc = (holder as ChatViewHolderLeft).viewMvc
                holder.setIsRecyclable(false)
                viewMvc.bindMessageModel(items[position])
            }
            VIEW_TYPE_RIGHT -> {
                val viewMvc = (holder as ChatViewHolderRight).viewMvc
                holder.setIsRecyclable(false)
                viewMvc.bindMessageModel(items[position])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ChatViewHolderLeft(val viewMvc: MessageLeftItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    class ChatViewHolderRight(val viewMvc: MessageRightItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    override fun onHeartClicked(messageModel: MessageListModel) {
        listener.onHeartClicked(messageModel)
    }

    companion object {
        private const val VIEW_TYPE_LEFT = 1
        private const val VIEW_TYPE_RIGHT = 2
    }
}