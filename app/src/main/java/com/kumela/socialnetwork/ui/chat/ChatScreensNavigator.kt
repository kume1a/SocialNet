package com.kumela.socialnetwork.ui.chat

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 29,October,2020
 **/

class ChatScreensNavigator @Inject constructor(private val navController: NavController) {
    fun navigateUp() {
        navController.navigateUp()
    }
}