package com.kumela.socialnetwork.ui.splash

import androidx.navigation.NavController
import com.kumela.socialnetwork.ui.common.utils.navigateSafely
import javax.inject.Inject

/**
 * Created by Toko on 27,September,2020
 **/

class SplashScreensNavigator @Inject constructor(private val navController: NavController) {
    fun toAuth() {
        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        navController.navigateSafely(action)
    }

    fun toHome() {
        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        navController.navigateSafely(action)
    }
}