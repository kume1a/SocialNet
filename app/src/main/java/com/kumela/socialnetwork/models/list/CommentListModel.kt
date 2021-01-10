package com.kumela.socialnetwork.models.list

/**
 * Created by Toko on 08,November,2020
 **/

data class CommentListModel(
    val posterId: String,
    val posterImageUri: String,
    val posterUsername: String,
    val comment: String,
    val timestamp: Long
)