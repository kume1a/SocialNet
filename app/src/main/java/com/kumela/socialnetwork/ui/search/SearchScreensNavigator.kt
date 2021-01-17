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

    fun toUserProfile(id: Int, imageUrl: String, name: String, bio: String) {
        val action = SearchFragmentDirections
            .actionGlobalUserProfileFragment(id, imageUrl, name, bio)
        navController.navigate(action)
    }
}