package com.kumela.socialnetwork.ui.post_image

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 16,October,2020
 **/

class PostImageScreensNavigator @Inject constructor(private val navController: NavController) {

    fun navigateUp() {
        navController.navigateUp()
    }
}