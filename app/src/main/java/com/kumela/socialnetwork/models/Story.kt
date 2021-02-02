package com.kumela.socialnetwork.models

/**
 * Created by Toko on 02,October,2020
 **/

data class Story(
    val id: Int,
    val userId: Int,
    val createdAt: Long,
    val imageUrl: String
)