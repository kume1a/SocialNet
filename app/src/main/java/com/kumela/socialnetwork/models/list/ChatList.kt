package com.kumela.socialnetwork.models.list

/**
 * Created by Toko on 31,October,2020
 **/

data class ChatList(
    val id: String? = null,
    val lastMessage: String? = null,
    val lastUpdated: Long? = null,
    val targetUid: String = "",
    val targetUsername: String = "",
    val targetImageUri: String = "",
    val unseenMessageCount: Int? = null,
    val targetIsOnline: Boolean = false
)