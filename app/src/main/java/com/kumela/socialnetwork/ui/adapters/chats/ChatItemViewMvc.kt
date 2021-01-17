package com.kumela.socialnetwork.ui.adapters.chats

import com.kumela.socialnetwork.models.list.ChatList
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 04,October,2020
 **/

interface ChatItemViewMvc : ObservableViewMvc<ChatItemViewMvc.Listener> {
    interface Listener {
        fun onChatClicked(chatList: ChatList)
    }

    fun bindChatListModel(chatList: ChatList)
    fun showSuggestionsHeader(visible: Boolean)
}