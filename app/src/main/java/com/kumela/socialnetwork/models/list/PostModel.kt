package com.kumela.socialnet.models.list

import com.google.firebase.database.Exclude
import com.kumela.socialnet.models.PostFields

/**
 * Created by Toko on 01,October,2020
 **/

data class PostModel(
    val id: String = "",
    val posterUid: String = "",
    val timestamp: Long = 0L,
    val postImageUri: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val header: String = "",
    val description: String = ""
) {
    @Exclude
    fun toMap(): Map<out String, Any> {
        return hashMapOf(
            PostFields.id to id,
            PostFields.posterUid to posterUid,
            PostFields.timestamp to timestamp,
            PostFields.postImageUri to postImageUri,
            PostFields.likeCount to likeCount,
            PostFields.commentCount to commentCount,
            PostFields.header to header,
            PostFields.description to description
        )
    }
}