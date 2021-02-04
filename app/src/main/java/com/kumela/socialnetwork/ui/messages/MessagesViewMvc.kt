package com.kumela.socialnetwork.ui.messages

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.ChatList
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface MessagesViewMvc : ObservableViewMvc<MessagesViewMvc.Listener> {
    interface Listener {
        fun onChatClicked(chatList: ChatList)
        fun onFriendClicked(user: User)
        fun onSearchClicked()
    }

    fun addUsers(users: List<User>)

    fun addChats(chats: List<ChatList>)
    fun addOrUpdateChat(chat: ChatList)
}