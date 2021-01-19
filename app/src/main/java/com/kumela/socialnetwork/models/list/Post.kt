package com.kumela.socialnetwork.models.list

import com.google.firebase.database.Exclude
import com.kumela.socialnetwork.models.PostFields

/**
 * Created by Toko on 01,October,2020
 **/

data class Post(
    val id: Int,
    val userId: Int,
    val createdAt: String,
    val imageUrl: String,
    val likeCount: Int,
    val commentCount: Int,
    val header: String,
    val description: String
) {
    @Exclude
    fun toMap(): Map<out String, Any> {
        return hashMapOf(
            PostFields.id to id,
            PostFields.posterUid to userId,
            PostFields.timestamp to createdAt,
            PostFields.postImageUri to imageUrl,
            PostFields.likeCount to likeCount,
            PostFields.commentCount to commentCount,
            PostFields.header to header,
            PostFields.description to description
        )
    }
}