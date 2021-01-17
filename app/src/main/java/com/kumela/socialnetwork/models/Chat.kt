package com.kumela.socialnetwork.models

/**
 * Created by Toko on 30,October,2020
 **/

data class Chat(
    val id: String = "",
    val lastMessage: String = "",
    val lastUpdated: Long = 0L
)