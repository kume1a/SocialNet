package com.kumela.socialnetwork.ui.adapters.messages

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.list.MessageList
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
        fun onMessageLongClick(message: MessageList)
    }

    private val items = arrayListOf<MessageList>()
    private val uid = ""

    fun addMessages(messages: List<MessageList>) {
        val oldSize = items.size
        items.addAll(messages)
        notifyItemRangeInserted(oldSize, messages.size)
    }

    fun updateMessage(message: MessageList) {
        val index = items.indexOfFirst { it.timestamp == message.timestamp }
        items[index] = message
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

    override fun onLongClick(message: MessageList) {
        listener.onMessageLongClick(message)
    }

    companion object {
        private const val VIEW_TYPE_LEFT = 1
        private const val VIEW_TYPE_RIGHT = 2
    }
}