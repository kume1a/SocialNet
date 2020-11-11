package com.kumela.socialnet.models.list

/**
 * Created by Toko on 02,November,2020
 **/

data class MessageListModel(
    val messageId: String,
    val senderId: String,
    val message: String,
    val timestamp: Long,
    var liked: Boolean,
    val sent: Boolean
)