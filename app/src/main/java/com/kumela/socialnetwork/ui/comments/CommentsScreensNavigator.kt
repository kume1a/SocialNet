package com.kumela.socialnet.ui.comments

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsScreensNavigator @Inject constructor(private val navController: NavController) {
    fun navigateUp() {
        navController.navigateUp()
    }

    fun toUserProfile(userId: String, userImageUri: String, userUsername: String) {
        val action =
            CommentsFragmentDirections.actionGlobalUserProfileFragment(userId, userImageUri, userUsername)
        navController.navigate(action)
    }

}