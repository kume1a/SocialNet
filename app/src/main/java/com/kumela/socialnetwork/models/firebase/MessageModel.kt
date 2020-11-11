package com.kumela.socialnet.models.firebase

import com.kumela.socialnet.models.MessageFields

/**
 * Created by Toko on 30,October,2020
 **/

data class MessageModel(
    val id: String = "",
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0,
    val liked: Boolean = false
) {
    fun toMap(): Map<out String, Any> {
        return hashMapOf(
//            MessageFields.id to id,
            MessageFields.senderId to senderId,
            MessageFields.message to message,
            MessageFields.timestamp to timestamp
//            MessageFields.liked to liked
        )
    }
}