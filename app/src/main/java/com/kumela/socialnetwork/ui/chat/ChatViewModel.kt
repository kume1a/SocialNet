package com.kumela.socialnetwork.ui.chat

import android.util.Log
import com.kumela.socialnetwork.models.firebase.MessageModel
import com.kumela.socialnetwork.network.firebase.ChatUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.network.firebase.helpers.DatabaseHelper
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 29,October,2020
 **/

class ChatViewModel(
    private val messageQueryPager: QueryPager<MessageModel>
) : ObservableViewModel<ChatViewModel.Listener>() {

    interface Listener {
        fun onMessagesFetched(messages: List<MessageModel>)
        fun onMessageReceived(messageModel: MessageModel)
    }

    private val messages = arrayListOf<MessageModel>()

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

    fun getMessages(): List<MessageModel> = messages

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

    fun sendMessage(chatId: String, messageModel: MessageModel) {
        ChatUseCase.sendMessage(chatId, messageModel)
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