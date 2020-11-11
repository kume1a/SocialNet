package com.kumela.socialnet.models.firebase

/**
 * Created by Toko on 08,November,2020
 **/

data class CommentModel(
    val userId: String = "",
    val timestamp: Long = 0,
    val comment: String = ""
)