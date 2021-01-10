package com.kumela.socialnetwork.ui.adapters.chats

import com.kumela.socialnetwork.models.list.ChatListModel
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 04,October,2020
 **/

interface ChatItemViewMvc : ObservableViewMvc<ChatItemViewMvc.Listener> {
    interface Listener {
        fun onChatClicked(chatListModel: ChatListModel)
    }

    fun bindChatListModel(chatListModel: ChatListModel)
    fun showSuggestionsHeader(visible: Boolean)
}