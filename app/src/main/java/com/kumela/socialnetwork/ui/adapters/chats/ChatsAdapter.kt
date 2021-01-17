package com.kumela.socialnetwork.ui.adapters.chats

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.list.ChatList
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 04,October,2020
 **/

class ChatsAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (ChatList) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>(), ChatItemViewMvc.Listener {

    private val items = arrayListOf<ChatList>()

    fun bindChats(chats: List<ChatList>) {
        items.clear()
        items.addAll(chats)
        notifyDataSetChanged()
    }

    fun addOrUpdateChat(chat: ChatList) {
        val index = items.indexOfFirst { chat.id == it.id }
        if (index == -1) {
            items.add(chat)
            notifyItemInserted(items.size - 1)
        } else {
            items[index] = chat
            notifyItemChanged(index)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val viewMvc = viewMvcFactory.newInstance(ChatItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return ChatViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentModel = items[position]

        val shouldShowSuggestionsHeader =
            (items.size == 1 && currentModel.id == null) || (currentModel.id == null && items[position - 1].id != null)

        holder.viewMvc.showSuggestionsHeader(shouldShowSuggestionsHeader)
        holder.viewMvc.bindChatListModel(currentModel)
    }

    override fun getItemCount(): Int = items.size

    class ChatViewHolder(val viewMvc: ChatItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    override fun onChatClicked(chatList: ChatList) {
        listener.invoke(chatList)
    }
}