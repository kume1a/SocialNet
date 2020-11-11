package com.kumela.socialnet.models.firebase


/**
 * Created by Toko on 24,October,2020
 **/

data class FeedModel(
    val postId: String,
    val posterUid: String,
    val posterUsername: String,
    val posterImageUri: String,
    val liked: Boolean,
    val timestamp: Long,
    val postImageUri: String,
    val likeCount: Int,
    val commentCount: Int,
    val header: String,
    val description: String,
)