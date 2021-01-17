package com.kumela.socialnetwork.models.list

/**
 * Created by Toko on 02,November,2020
 **/

data class MessageList(
    val messageId: String,
    val senderId: String,
    val message: String,
    val timestamp: Long,
    var liked: Boolean,
    val sent: Boolean
)