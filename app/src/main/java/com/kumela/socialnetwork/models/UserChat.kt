package com.kumela.socialnetwork.models

/**
 * Created by Toko on 31,October,2020
 **/

data class UserChat(
    val chatId: String = "",
    val unseenMessageCount: Int = 0,
    val targetId: String = ""
)