package com.kumela.socialnetwork.models

data class Reply(
    val userId: Int,
    val commentId: Int,
    val userName: String,
    val userImageUrl: String,
    val createdAt: Long,
    val body: String,
)