@file:Suppress("unused")

package com.kumela.socialnetwork.models

/**
 * Created by Toko on 18,October,2020
 **/

object UserFields {
    const val id = "id"
    const val username = "username"
    const val imageUri = "imageUri"
    const val lastOnline = "lastOnline"
}

object UserExtraInfoFields {
    const val bio = "bio"
    const val followerCount = "followerCount"
    const val followingCount = "followingCount"
    const val postCount = "postCount"
}

object PostFields {
    const val id = "id"
    const val posterUid = "posterUid"
    const val timestamp = "timestamp"
    const val postImageUri = "postImageUri"
    const val likeCount = "likeCount"
    const val commentCount = "commentCount"
    const val header = "header"
    const val description = "description"
    const val commentsTableId = "commentsTableId"
}

object MessageFields {
    const val id = "id"
    const val senderId = "senderId"
    const val message = "message"
    const val timestamp = "timestamp"
    const val liked = "liked"
}