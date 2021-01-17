package com.kumela.socialnetwork.models

/**
 * Created by Toko on 05,October,2020
 **/

data class User(
    val id: String = "",
    val username: String = "",
    val imageUri: String = "",
    val lastOnline: Long = 0L
)