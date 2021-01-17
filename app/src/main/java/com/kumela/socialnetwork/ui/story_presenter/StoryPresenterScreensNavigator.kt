package com.kumela.socialnetwork.ui.story_presenter

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 10,November,2020
 **/

class StoryPresenterScreensNavigator @Inject constructor(private val navController: NavController) {
    fun navigateUp() {
        navController.navigateUp()
    }

    fun toUserProfile(userId: String, userImageUri: String, userUsername: String) {
//        val action =
//            StoryPresenterFragmentDirections.actionGlobalUserProfileFragment(userId, userImageUri, userUsername)
//        navController.navigate(action)
    }
}