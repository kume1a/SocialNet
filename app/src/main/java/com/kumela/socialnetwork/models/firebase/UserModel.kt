package com.kumela.socialnetwork.models.firebase

/**
 * Created by Toko on 05,October,2020
 **/

data class UserModel(
    val id: String = "",
    val username: String = "",
    val imageUri: String = "",
    val lastOnline: Long = 0L
)