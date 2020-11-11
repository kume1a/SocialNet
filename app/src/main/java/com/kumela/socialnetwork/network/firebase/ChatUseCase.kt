package com.kumela.socialnet.network.firebase

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.kumela.socialnet.common.UseCase
import com.kumela.socialnet.models.firebase.ChatModel
import com.kumela.socialnet.models.MessageFields
import com.kumela.socialnet.models.firebase.MessageModel
import com.kumela.socialnet.network.firebase.helpers.DatabaseHelper
import com.kumela.socialnet.network.firebase.helpers.FunctionsHelper
import java.util.*

/**
 * Created by Toko on 30,October,2020
 **/

object ChatUseCase : UseCase() {
    private var messagesChildEventListener: ChildEventListener? = null
    private var chatChildEventListener: ValueEventListener? = null

    fun getUserChatsRef(): DatabaseReference =
        DatabaseHelper.getUserChatsTableRef().child(UserUseCase.uid)

    fun sendMessage(chatId: String, messageModel: MessageModel) {
        FunctionsHelper.sendMessage(chatId, messageModel)
    }

    fun registerMessageListener(
        uuid: UUID,
        chatId: String,
        onMessageReceived: (MessageModel) -> Unit,
        onCanceled: (DatabaseError) -> Unit
    ) {
        messagesChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (isActive(uuid)) {
                    onMessageReceived.invoke(snapshot.getValue<MessageModel>()!!)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                if (isActive(uuid)) {
                    onCanceled.invoke(error)
                }
            }
        }

        DatabaseHelper
            .getMessageTableRef()
            .child(chatId)
            .limitToLast(1)
            .addChildEventListener(messagesChildEventListener!!)
    }

    fun unregisterMessageListener(chatId: String) {
        messagesChildEventListener?.let {
            DatabaseHelper
                .getMessageTableRef()
                .child(chatId)
                .removeEventListener(it)
        }
        messagesChildEventListener = null
    }

    fun registerChatUpdateListener(
        uuid: UUID,
        chatId: String,
        onChatUpdated: (ChatModel) -> Unit,
        onCanceled: (DatabaseError) -> Unit
    ) {
        chatChildEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isActive(uuid)) {
                    val chatModel = snapshot.children.first().getValue<ChatModel>()!!
                    onChatUpdated.invoke(chatModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isActive(uuid)) {
                    onCanceled.invoke(error)
                }
            }
        }

        DatabaseHelper
            .getChatTableRef()
            .orderByChild("id")
            .equalTo(chatId)
            .limitToLast(1)
            .addValueEventListener(chatChildEventListener!!)
    }

    fun unregisterChatUpdateListener(chatId: String) {
        chatChildEventListener?.let { DatabaseHelper
            .getChatTableRef()
            .child(chatId)
            .removeEventListener(it)
        }
        chatChildEventListener = null
    }

    fun likeOrDislikeMessage(chatId: String, messageId: String, liked: Boolean) {
        DatabaseHelper
            .getMessageTableRef()
            .child(chatId)
            .child(messageId)
            .child(MessageFields.liked)
            .setValue(liked)
    }
}