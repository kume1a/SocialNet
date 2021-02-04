package com.kumela.socialnetwork.ui.chat

import com.kumela.socialnetwork.models.list.MessageList
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface ChatViewMvc : ObservableViewMvc<ChatViewMvc.Listener> {
    interface Listener {
        fun onBackPressed()

        fun onPlusClicked()
        fun onMinusClicked()
        fun onPictureClicked()
        fun onVideoClicked()
        fun onAudioClicked()

        fun onSendClicked()

        fun onKeyboardUp()

        fun onScrolledToTop()
        fun onMessageLongClick(message: MessageList)
    }

    fun bindToolbarText(text: String)

    fun scrollToBottom()

    fun showUploadOptions()
    fun hideUploadOptions()

    fun getMessage(): String
    fun clearInputField()

    fun addMessages(messages: List<MessageList>)
    fun updateMessage(message: MessageList)
}