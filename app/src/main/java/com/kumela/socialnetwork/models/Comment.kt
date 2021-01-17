package com.kumela.socialnetwork.models

/**
 * Created by Toko on 08,November,2020
 **/

data class Comment(
    val userId: String = "",
    val timestamp: Long = 0,
    val comment: String = ""
)