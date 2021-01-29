package com.kumela.socialnetwork.models

/**
 * Created by Toko on 08,November,2020
 **/

data class Comment(
    val id: Int,
    val userId: Int,
    val postId: Int,
    val userName: String,
    val userImageUrl: String,
    val createdAt: Long,
    val body: String,
    val firstReplies: MutableList<Reply>?,
    val replyCount: Int?
)