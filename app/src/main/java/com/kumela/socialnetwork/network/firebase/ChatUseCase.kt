package com.kumela.socialnetwork.network.firebase

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.kumela.socialnetwork.common.UseCase
import com.kumela.socialnetwork.models.Chat
import com.kumela.socialnetwork.models.MessageFields
import com.kumela.socialnetwork.models.Message
import com.kumela.socialnetwork.network.firebase.helpers.DatabaseHelper
import com.kumela.socialnetwork.network.firebase.helpers.FunctionsHelper
import java.util.*

/**
 * Created by Toko on 30,October,2020
 **/

object ChatUseCase : UseCase() {
    private var messagesChildEventListener: ChildEventListener? = null
    private var chatChildEventListener: ValueEventListener? = null

    fun getUserChatsRef(): DatabaseReference =
        DatabaseHelper.getUserChatsTableRef().child(UserUseCase.uid)

    fun sendMessage(chatId: String, message: Message) {
        FunctionsHelper.sendMessage(chatId, message)
    }

    fun registerMessageListener(
        uuid: UUID,
        chatId: String,
        onMessageReceived: (Message) -> Unit,
        onCanceled: (DatabaseError) -> Unit
    ) {
        messagesChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (isActive(uuid)) {
                    onMessageReceived.invoke(snapshot.getValue<Message>()!!)
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
        onChatUpdated: (Chat) -> Unit,
        onCanceled: (DatabaseError) -> Unit
    ) {
        chatChildEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isActive(uuid)) {
                    val chatModel = snapshot.children.first().getValue<Chat>()!!
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