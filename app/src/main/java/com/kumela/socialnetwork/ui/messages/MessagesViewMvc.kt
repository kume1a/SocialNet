package com.kumela.socialnetwork.ui.messages

import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.models.list.ChatListModel
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface MessagesViewMvc : ObservableViewMvc<MessagesViewMvc.Listener> {
    interface Listener {
        fun onChatClicked(chatListModel: ChatListModel)
        fun onFriendClicked(userModel: UserModel)
        fun onSearchClicked()
    }

    fun bindFriends(users: List<UserModel>)

    fun bindChats(chats: List<ChatListModel>)
    fun addOrUpdateChat(chat: ChatListModel)
}