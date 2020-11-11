package com.kumela.socialnet.ui.story_uploder

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 07,November,2020
 **/

class StoryUploaderScreensNavigator @Inject constructor(private val navController: NavController) {
    fun navigateUp() {
        navController.navigateUp()
    }

}