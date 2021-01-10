package com.kumela.socialnetwork.ui.user_list

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 05,November,2020
 **/

class UserListScreensNavigator @Inject constructor(private val navController: NavController) {
    fun navigateUp() {
        navController.navigateUp()
    }

    fun toUserProfile(userId: String, userImageUri: String, userUsername: String) {
        val action =
            UserListFragmentDirections.actionGlobalUserProfileFragment(userId, userImageUri, userUsername)
        navController.navigate(action)
    }
}