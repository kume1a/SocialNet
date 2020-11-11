package com.kumela.socialnet.ui.adapters.chats

import com.kumela.socialnet.models.list.ChatListModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

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