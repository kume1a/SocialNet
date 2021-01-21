package com.kumela.socialnetwork.models

import com.google.gson.annotations.SerializedName


/**
 * Created by Toko on 24,October,2020
 **/

data class Feed(
    val id: Int,
    val userId: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_image_url") val userImageUrl: String,
    @SerializedName("user_bio") val userBio: String,
    @SerializedName("is_liked") val isLiked: Boolean,
    val createdAt: Long,
    val imageUrl: String,
    @SerializedName("like_count") val likeCount: Int,
    val commentCount: Int,
    val header: String,
    val description: String,
)