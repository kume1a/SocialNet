package com.kumela.socialnet.models.firebase

/**
 * Created by Toko on 15,October,2020
 **/

data class UserExtraInfoModel(
    val bio: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val postCount: Int = 0
)