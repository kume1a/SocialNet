package com.kumela.socialnet.models.firebase

/**
 * Created by Toko on 31,October,2020
 **/

data class UserChatModel(
    val chatId: String = "",
    val unseenMessageCount: Int = 0,
    val targetId: String = ""
)