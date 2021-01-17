package com.kumela.socialnetwork.ui.chat

import com.kumela.socialnetwork.models.list.MessageList
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface ChatViewMvc : ObservableViewMvc<ChatViewMvc.Listener> {
    interface Listener {
        fun onBackPressed()

        fun onEmojiClicked()
        fun onShareFileClicked()
        fun onSendClicked()

        fun onMessageBoxFocused()
        fun onMessageBoxFocusLost()

        fun onScrolledToTop()
        fun onHeartClicked(message: MessageList)
    }

    fun bindToolbarText(text: String)

    fun transitionToEnd()
    fun transitionToStart()

    fun getCurrentMessage(): String
    fun clearMessageField()

    fun addMessage(message: MessageList)
    fun bindMessages(messages: List<MessageList>)
    fun addMessages(messages: List<MessageList>)
    fun updateMessage(message: MessageList)
}