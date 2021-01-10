package com.kumela.socialnetwork.models.firebase

/**
 * Created by Toko on 30,October,2020
 **/

data class ChatModel(
    val id: String = "",
    val lastMessage: String = "",
    val lastUpdated: Long = 0L
)