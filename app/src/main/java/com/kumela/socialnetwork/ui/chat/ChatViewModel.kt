package com.kumela.socialnetwork.ui.chat

import android.util.Log
import com.kumela.socialnetwork.models.Message
import com.kumela.socialnetwork.network.firebase.ChatUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.network.firebase.helpers.DatabaseHelper
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 29,October,2020
 **/

class ChatViewModel(
    private val messageQueryPager: QueryPager<Message>
) : ObservableViewModel<ChatViewModel.Listener>() {

    interface Listener {
        fun onMessagesFetched(messages: List<Message>)
        fun onMessageReceived(message: Message)
    }

    private val messages = arrayListOf<Message>()

    // necessary var for child update listener
    private var firstMessageReceived = false

    init {
        UserUseCase.registerListener(uuid)
        ChatUseCase.registerListener(uuid)
        messageQueryPager.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        UserUseCase.unregisterListener(uuid)
        ChatUseCase.unregisterListener(uuid)
        messageQueryPager.unregisterListener(uuid)
    }

    fun getMessages(): List<Message> = messages

    fun fetchNextMessagesPageAndNotify(chatId: String) {
        messageQueryPager.fetchNextPageAndNotify(
            uuid, DatabaseHelper.getMessageTableRef().child(chatId),
            { messages ->
                if (messages != null && messages.isNotEmpty()) {
                    this.messages.addAll(messages)
                    for (listener in listeners) {
                        listener.onMessagesFetched(messages)
                    }
                }
            },
            { databaseError ->
                Log.e(
                    javaClass.simpleName,
                    "fetchMessagesAndNotify: ",
                    databaseError.toException()
                )
            }
        )
    }

    fun sendMessage(chatId: String, message: Message) {
        ChatUseCase.sendMessage(chatId, message)
    }

    fun registerMessageListener(chatId: String) {
        ChatUseCase.registerMessageListener(uuid, chatId,
            { messageModel ->
                if (firstMessageReceived) {
                    for (listener in listeners) {
                        listener.onMessageReceived(messageModel)
                    }
                }
                firstMessageReceived = true
            },
            { databaseError ->
                Log.e(javaClass.simpleName, "registerMessageListener: ", databaseError.toException())
            })
    }

    fun unregisterMessageListener(chatId: String) {
        ChatUseCase.unregisterMessageListener(chatId)
    }

    fun likeMessage(chatId: String, messageId: String, liked: Boolean) {
        ChatUseCase.likeOrDislikeMessage(chatId, messageId, liked)
    }
}