package com.kumela.socialnetwork.ui.search

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 18,October,2020
 **/

class SearchScreensNavigator @Inject constructor(private val navController: NavController) {
    fun navigateUp() {
        navController.navigateUp()
    }

    fun toUserProfile(uid: String, imageUri: String, username: String) {
        val action = SearchFragmentDirections.actionGlobalUserProfileFragment(
            uid, imageUri, username
        )
        navController.navigate(action)
    }
}